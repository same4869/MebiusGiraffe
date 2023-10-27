package com.pokemon.mebius.giraffe.base.common

import android.app.ActivityManager
import android.content.Context

object GiraffeUtils {
    fun isMainProcess(context: Context?): Boolean {
        return context != null && context.packageName == getCurrentProcessName(context)
    }

    private fun getCurrentProcessName(context: Context): String {
        val pid = android.os.Process.myPid()
        var processName = ""
        val manager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (process in manager.runningAppProcesses) {
            if (process.pid == pid) {
                processName = process.processName
            }
        }
        return processName
    }
}