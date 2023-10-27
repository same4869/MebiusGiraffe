package com.pokemon.mebius.giraffe.base.common

import android.app.ActivityManager
import android.content.Context
import android.os.Looper
import com.pokemon.mebius.giraffe.base.GiraffeMonitorProtocol
import com.pokemon.mebius.giraffe.base.entities.GiraffeExceptionInfo
import com.pokemon.mebius.giraffe.base.entities.GiraffeHttpLogInfo

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

    /**
     * 把功能的名字转换为对应存储数据对象
     * */
    fun nameToInfoClass(name: String): Class<*> {
        return when (name) {
            GiraffeMonitorProtocol.EXCEPTION.name -> GiraffeExceptionInfo::class.java
            GiraffeMonitorProtocol.NET.name -> GiraffeHttpLogInfo::class.java
            else -> GiraffeHttpLogInfo::class.java
        }
    }

    fun isMainThread(threadId: Long): Boolean {
        return Looper.getMainLooper().thread.id == threadId
    }
}