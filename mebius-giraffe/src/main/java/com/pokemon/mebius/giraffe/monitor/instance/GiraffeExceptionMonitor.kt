package com.pokemon.mebius.giraffe.monitor.instance

import android.content.Context
import com.pokemon.mebius.commlib.utils.APPLICATION
import com.pokemon.mebius.giraffe.R
import com.pokemon.mebius.giraffe.base.GiraffeLog
import com.pokemon.mebius.giraffe.base.GiraffeMonitorProtocol
import com.pokemon.mebius.giraffe.base.common.GiraffeUtils
import com.pokemon.mebius.giraffe.base.common.toastInThread
import com.pokemon.mebius.giraffe.base.entities.GiraffeExceptionInfo
import com.pokemon.mebius.giraffe.monitor.GiraffeMonitor
import com.pokemon.mebius.giraffe.storage.GiraffeStorage

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/6/9
 */

class DoNotSleepException(e : Throwable) : Throwable(e)

internal class GiraffeExceptionMonitor(override var isOpen: Boolean = false) :
    GiraffeMonitorProtocol {
    override fun open(context: Context) {
        val defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            saveCrash(throwable, thread, defaultExceptionHandler)
        }
        isOpen = true
    }

    override fun close() {
        isOpen = false
    }

    override fun getMonitorInfo(): GiraffeMonitorProtocol.MonitorInfo = GiraffeMonitorProtocol.EXCEPTION

    fun saveCrash(
        e: Throwable,
        thread: Thread,
        defaultExceptionHandler: Thread.UncaughtExceptionHandler? = null
    ) {
        if (!isOpen) return

        val doNotSleep = e is DoNotSleepException

        toastInThread(APPLICATION.resources.getString(R.string.giraffe_exception_toast))

        if(!doNotSleep) {
            Thread.sleep(1000) // 把toast给弹出来
        }

        val realException = if(doNotSleep){
            e.cause ?: e
        } else {
            e
        }

        val exceptionInfo = translateThrowableToExceptionInfo(
            realException,
            Thread.currentThread().name)
        GiraffeStorage.saveSync(exceptionInfo)
        //main thead 下崩溃
        if (GiraffeUtils.isMainThread(thread.id)) {
            if(!doNotSleep) {
                Thread.sleep(1500)
            }
            defaultExceptionHandler?.uncaughtException(thread, e)
        }
    }

    private fun translateThrowableToExceptionInfo(
        e: Throwable,
        currentThread: String
    ): GiraffeExceptionInfo {
        val exceptionInfo = GiraffeExceptionInfo()
        exceptionInfo.apply {
            crashTraceStr = GiraffeLog.getStackTraceString(e)
            exceptionName = e.javaClass.name
            simpleMessage = e.message ?: ""
            threadName = currentThread
            time = System.currentTimeMillis()
            pageName = GiraffeMonitor.getCurrentPage()
        }

        return exceptionInfo
    }

}