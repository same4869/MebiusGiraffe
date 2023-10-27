package com.pokemon.mebius.giraffe.base

import android.content.Context
import android.content.SharedPreferences

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/6/9
 */

object GiraffeSettings {

    private val SP_NAME = "giraffe_sp"
    private val AUTO_OPEN_GIRAFFE = "auto_open"

    fun autoOpenGiraffe(context: Context, autoOpen: Boolean) {
        setBooleanValue(
            context,
            AUTO_OPEN_GIRAFFE,
            autoOpen
        )
    }

    fun autoOpenGiraffe(context: Context) = getSp(context).getBoolean(
        AUTO_OPEN_GIRAFFE, false
    )

    fun autoOpen(context: Context, monitorName: String) =
        getSp(context).getBoolean(monitorName, false)

    fun setAutoOpenFlag(context: Context, monitorName: String, autoOpen: Boolean) {
        setBooleanValue(context, monitorName, autoOpen)
    }

    private fun getSp(context: Context): SharedPreferences {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    }

    private fun getSpEdit(context: Context): SharedPreferences.Editor {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit()
    }

    private fun setBooleanValue(context: Context, key: String, value: Boolean) {
        getSpEdit(context).putBoolean(key, value).commit()
    }

}