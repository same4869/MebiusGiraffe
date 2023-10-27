package com.pokemon.mebius.giraffe.storage

import android.app.Application
import com.pokemon.mebius.giraffe.base.GiraffeLog
import com.pokemon.mebius.giraffe.base.TAG_STORAGE
import com.pokemon.mebius.giraffe.base.entities.GiraffeExceptionInfo
import com.pokemon.mebius.giraffe.base.entities.GiraffeHttpLogInfo
import com.pokemon.mebius.giraffe.base.entities.GiraffeInfoProtocol
import com.pokemon.mebius.giraffe.config.GiraffeDaoProviderConfig
import org.greenrobot.greendao.AbstractDao
import org.greenrobot.greendao.Property
import org.greenrobot.greendao.query.WhereCondition

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/6/8
 */

internal class GiraffeGreenDaoDbManager(
    val application: Application,
    extraDaoProvider: ArrayList<GiraffeDaoProviderConfig> = ArrayList()
) : GiraffeStorageProtocol {

    private val DB_NAME = "giraffe-apm"
    private var daoSession: DaoSession?
    private val daoMap = HashMap<String, AbstractDao<Any, Long>>()

    init {
        daoSession = DaoMaster(
            DaoMaster.DevOpenHelper(
                application,
                DB_NAME
            ).writableDb
        ).newSession()
        getAllDao(daoSession!!, extraDaoProvider).forEach {
            daoMap[it.clazz.simpleName] = it.dao
        }
    }

    override fun <T : GiraffeInfoProtocol> update(
        clazz: Class<T>,
        obj: GiraffeInfoProtocol,
        id: Long
    ) {
        val daoImpl = daoImpl(obj.javaClass) ?: return
        val storageData = getDataById(clazz, id)
        if (storageData == null) {
            daoImpl.save(obj)
        } else {
            daoImpl.update(obj)
        }
    }

    override fun <T : GiraffeInfoProtocol> query(clazz: Class<T>, id: Long): T? {
        return getDataById(clazz, id)
    }

    override fun save(obj: GiraffeInfoProtocol) {
        val daoImpl = daoImpl(obj.javaClass) ?: return
        daoImpl.save(obj)
        GiraffeLog.d(TAG_STORAGE, "save data time : ${obj.time} name : ${obj.pageName} class:${obj.javaClass.name}")
    }

    override fun <T : Any> delete(clazz: Class<T>, id: Long) {
        daoImpl(clazz)?.deleteByKey(id)
    }

    override fun <T : GiraffeInfoProtocol> distinct(
        clazz: Class<T>,
        columnName: String
    ): List<String> {
        val dao = daoImpl(clazz) ?: return emptyList()
        val distinctSQL = "SELECT DISTINCT $columnName FROM ${dao.tablename};"
        val cursor = daoSession?.database?.rawQuery(distinctSQL, null) ?: return emptyList()
        val resList = ArrayList<String>()
        while (cursor.moveToNext()) {
            resList.add(cursor.getString(0))
        }
        return resList
    }

    override fun <T : Any> count(clazz: Class<T>): Long {
        return daoImpl(clazz)?.queryBuilder()?.count() ?: 0
    }

    fun <T : GiraffeInfoProtocol> delete(clazz: Class<T>, condition: WhereCondition) {
        getDatas(clazz, condition).forEach {
            daoImpl(clazz)?.delete(it)
        }
    }

    private fun <T : GiraffeInfoProtocol> getDataById(clazz: Class<T>, id: Long): T? {
        return daoImpl(clazz)?.loadByRowId(id)
    }

    fun <T : GiraffeInfoProtocol> getDatas(
        clazz: Class<T>,
        condition: WhereCondition? = null,
        sortField: String = "time",
        count: Int = 0,
        offset: Int = 0,
        orderDesc: Boolean = false
    ): List<T> {
        val dao = daoImpl(clazz)
        val property = getProperties(dao, sortField)
        val queryBuilder = dao?.queryBuilder()
        if (count != 0) {
            queryBuilder?.limit(count)
        }
        if (offset != 0) {
            queryBuilder?.offset(offset)
        }
        GiraffeLog.d("orderDesc:$orderDesc")
        if (orderDesc) {
            queryBuilder?.orderDesc(property)
        } else {
            queryBuilder?.orderAsc(property)
        }
        if (condition != null) {
            queryBuilder?.where(condition)
        }

        return queryBuilder?.build()?.list() ?: emptyList()
    }

    fun <T : GiraffeInfoProtocol> getDataByConditions(
        clazz: Class<T>,
        condition1: WhereCondition,
        condition2: WhereCondition,
        sortField: String = "time",
        count: Int = 0,
        offset: Int = 0,
        orderDesc: Boolean = false,
        vararg condition3: WhereCondition
    ): List<T> {
        val dao = daoImpl(clazz)
        val property = getProperties(dao, sortField)
        val queryBuilder = dao?.queryBuilder()
        if (count != 0) {
            queryBuilder?.limit(count)
        }
        if (offset != 0) {
            queryBuilder?.offset(offset)
        }
        GiraffeLog.d("orderDesc:$orderDesc")
        if (orderDesc) {
            queryBuilder?.orderDesc(property)
        } else {
            queryBuilder?.orderAsc(property)
        }

        queryBuilder?.whereOr(condition1, condition2, *condition3)
        return queryBuilder?.build()?.list() ?: emptyList()
    }


    private fun <T : Any> getProperties(
        dao: AbstractDao<T, Long>?,
        field: String
    ): Property? {
        return dao?.properties?.find { it.name == field }
    }

    override fun <T : Any> clear(clazz: Class<T>) {
        daoImpl(clazz)?.deleteAll()
    }

    private fun <T : Any> daoImpl(entitiesClass: Class<T>): AbstractDao<T, Long>? {
        return daoMap[entitiesClass.simpleName] as AbstractDao<T, Long>
    }

    private fun getAllDao(
        daoSession: DaoSession,
        extraDaoProvider: ArrayList<GiraffeDaoProviderConfig>
    ): List<GiraffeDaoProviderConfig> {
        return ArrayList<GiraffeDaoProviderConfig>().apply {
            addAll(extraDaoProvider)
            add(
                GiraffeDaoProviderConfig(
                    GiraffeHttpLogInfo::class.java as Class<Any>,
                    daoSession.wolfHttpLogInfoDao as AbstractDao<Any, Long>
                )
            )
            add(
                GiraffeDaoProviderConfig(
                    GiraffeExceptionInfo::class.java as Class<Any>,
                    daoSession.wolfExceptionInfoDao as AbstractDao<Any, Long>
                )
            )
        }
    }

}