package com.pokemon.mebius.giraffe.storage

import com.pokemon.mebius.giraffe.base.entities.GiraffeInfoProtocol


/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/6/8
 */

interface GiraffeStorageProtocol {
    fun save(obj: GiraffeInfoProtocol)

    fun <T : GiraffeInfoProtocol> query(clazz: Class<T>, id: Long): T?

    fun <T : Any> delete(clazz: Class<T>, id: Long)

    fun <T : Any> clear(clazz: Class<T>)

    fun <T : Any> count(clazz: Class<T>): Long

    fun <T : GiraffeInfoProtocol> update(
        clazz: Class<T>,
        obj: GiraffeInfoProtocol,
        id: Long
    )

    fun <T : GiraffeInfoProtocol> distinct(clazz: Class<T>, columnName: String): List<String>
}