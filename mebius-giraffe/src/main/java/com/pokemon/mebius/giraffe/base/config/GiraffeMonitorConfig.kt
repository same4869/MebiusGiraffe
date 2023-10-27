package com.pokemon.mebius.giraffe.base.config

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/6/9
 *
 *
 *  * @property autoOpenMonitors 启动应用后自动打开哪些监控
 *
 */

class GiraffeMonitorConfig (
    val autoOpenMonitors: HashSet<String> = HashSet()
)