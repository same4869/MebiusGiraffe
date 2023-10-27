package com.pokemon.mebius.giraffe.storage

import android.app.Application
import com.pokemon.mebius.giraffe.base.GiraffeLog
import com.pokemon.mebius.giraffe.base.GiraffeMonitorProtocol
import com.pokemon.mebius.giraffe.base.common.GiraffeAsync
import com.pokemon.mebius.giraffe.base.common.GiraffeUtils
import com.pokemon.mebius.giraffe.base.entities.GiraffeExceptionInfo
import com.pokemon.mebius.giraffe.base.entities.GiraffeHttpLogInfo
import com.pokemon.mebius.giraffe.base.entities.GiraffeInfoProtocol
import com.pokemon.mebius.giraffe.config.GiraffeStorageConfig
import io.reactivex.disposables.Disposable
import org.greenrobot.greendao.query.WhereCondition
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/6/8
 */

object GiraffeStorage {
    private lateinit var mApp: Application
    var mConfig = GiraffeStorageConfig()
    private var eventListeners = ArrayList<EventListener>()
    private val disposableList = ArrayList<Disposable?>()

    interface EventListener {
        fun onStorageData(obj: Any)
    }

    private val DB_THREAD = ThreadPoolExecutor(5, 10, 10, TimeUnit.SECONDS, LinkedBlockingDeque(),
        ThreadFactory { r ->
            Thread(r, "wolf_db_manager_thread${System.currentTimeMillis()}")
        }).apply {
        allowCoreThreadTimeOut(true)
    }

    private val greenDaoDbManage by lazy {
        GiraffeGreenDaoDbManager(mApp, mConfig.greenDaoProvider)
    }

    fun init(application: Application, config: GiraffeStorageConfig) {
        mApp = application
        mConfig = config

        //one session 存在的数据
        GiraffeAsync.asyncRun({
            mConfig.storageInOneSessionData.forEach {
                greenDaoDbManage.clear(GiraffeUtils.nameToInfoClass(it))
            }
        }, DB_THREAD)

        //超出最大限制的数据
        for (info in mConfig.dataMaxSaveCountLimit.entries) {
            val jclass = GiraffeUtils.nameToInfoClass(info.key)
            val currentCount = greenDaoDbManage.count(jclass)
            if (currentCount > info.value) {
                greenDaoDbManage.clear(jclass)
            }
        }

        GiraffeLog.d("GiraffeStorage init success!!")
    }

    fun <T : GiraffeInfoProtocol> getAll(
        ktClass: Class<T>,
        condition: WhereCondition? = null,
        sortField: String = GiraffeInfoProtocol.PROPERTIES_TIME,
        count: Int = 0,
        offset: Int = 0,
        orderDesc: Boolean = false,
        loadResult: (exceptionList: List<T>) -> Unit
    ) {
        GiraffeAsync.asyncRunWithResult({
            greenDaoDbManage.getDatas(
                ktClass,
                condition,
                sortField,
                count,
                offset,
                orderDesc = orderDesc
            )
        }, DB_THREAD, {
            loadResult(it)
        })
    }

    fun <T : GiraffeInfoProtocol> queryDataByWhereOrCondition(
        ktClass: Class<T>,
        condition: WhereCondition,
        condition2: WhereCondition,
        sortField: String = GiraffeInfoProtocol.PROPERTIES_TIME,
        count: Int = 0,
        offset: Int = 0,
        orderDesc: Boolean = false,
        loadResult: (exceptionList: List<T>) -> Unit
    ) {
        GiraffeAsync.asyncRunWithResult({
            greenDaoDbManage.getDataByConditions(
                ktClass,
                condition,
                condition2,
                sortField,
                count,
                offset,
                orderDesc
            )
        }, DB_THREAD, {
            loadResult(it)
        })
    }

    fun save(obj: GiraffeInfoProtocol) {
        try {
            val dis = GiraffeAsync.asyncRun({
                greenDaoDbManage.save(obj)
            }, DB_THREAD, {
                notifyEventListenerNewDataSave(obj)
                disposableList.removeAll { it == null || it.isDisposed }
            })
            disposableList.add(dis)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun saveSync(obj: GiraffeInfoProtocol) {
        greenDaoDbManage.save(obj)
        notifyEventListenerNewDataSave(obj)
    }

    private fun notifyEventListenerNewDataSave(obj: Any) {
        eventListeners.forEach {
            it.onStorageData(obj)
        }
    }

    fun <T : Any> clear(clazz: Class<T>) {
        GiraffeAsync.asyncRun({ greenDaoDbManage.clear(clazz) }, DB_THREAD)
    }

    private fun clearDataByMonitorName(monitorName: String) {
        when (monitorName) {
            GiraffeMonitorProtocol.EXCEPTION.name -> {
                clear(GiraffeExceptionInfo::class.java)
            }

            GiraffeMonitorProtocol.NET.name -> {
                clear(GiraffeHttpLogInfo::class.java)
            }

        }
    }

    fun clearAllData() {
        clearDataByMonitorName(GiraffeMonitorProtocol.EXCEPTION.name)
        clearDataByMonitorName(GiraffeMonitorProtocol.NET.name)
    }

    fun clearNetData(finishCallback: () -> Unit = {}) {
        GiraffeAsync.asyncRun(
            { greenDaoDbManage.clear(GiraffeHttpLogInfo::class.java) },
            DB_THREAD,
            finishCallback
        )
    }
}