package com.pokemon.mebius.giraffe.base

import android.content.Context
import com.pokemon.mebius.commlib.utils.APPLICATION
import com.pokemon.mebius.giraffe.R

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/6/9
 */

interface GiraffeMonitorProtocol {
    companion object {
        val EXCEPTION = MonitorInfo(
            "exception",
            APPLICATION.resources.getString(R.string.giraffe_feat_exception)
        )

        //网络日志监控
        val NET = MonitorInfo("net", APPLICATION.resources.getString(R.string.giraffe_feat_net))
    }

    class MonitorInfo(
        val name: String,
        val znName: String,
        val showInExternal: Boolean = true,
        val dataCanClear: Boolean = true
    )

    fun open(context: Context)

    fun close()

    fun getMonitorInfo(): MonitorInfo

    var isOpen: Boolean
}