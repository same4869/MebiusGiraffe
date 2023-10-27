package com.pokemon.mebius.giraffe.monitor

import android.app.Activity
import android.app.Application
import com.pokemon.mebius.giraffe.base.GiraffeLog
import com.pokemon.mebius.giraffe.base.GiraffeMonitorProtocol
import com.pokemon.mebius.giraffe.base.GiraffeSettings
import com.pokemon.mebius.giraffe.base.TAG_MONITOR
import com.pokemon.mebius.giraffe.base.common.GiraffeActivityLifecycleWrapper
import com.pokemon.mebius.giraffe.base.config.GiraffeMonitorConfig
import com.pokemon.mebius.giraffe.monitor.instance.GiraffeExceptionMonitor
import com.pokemon.mebius.giraffe.monitor.instance.GiraffeNetMonitor
import okhttp3.Interceptor
import java.lang.ref.WeakReference

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/6/9
 */

object GiraffeMonitor {
    private val monitorMap = LinkedHashMap<String, GiraffeMonitorProtocol>()
    lateinit var application: Application
    private var isInit = false
    var mConfig: GiraffeMonitorConfig = GiraffeMonitorConfig()
    private var appCurrentActivity: WeakReference<Activity?>? = null    //当前应用正在展示的Activity
    private var pageChangeListeners = HashSet<PageChangeListener>()

    init {
        monitorMap.apply {
            put(GiraffeMonitorProtocol.EXCEPTION.name, GiraffeExceptionMonitor())
            put(GiraffeMonitorProtocol.NET.name, GiraffeNetMonitor())
        }

    }

    fun init(application: Application, config_: GiraffeMonitorConfig) {
        GiraffeLog.d("GiraffeMonitor init! isInit:$isInit")
        if (isInit) return

        mConfig = config_
        GiraffeMonitor.application = application

        application.registerActivityLifecycleCallbacks(object : GiraffeActivityLifecycleWrapper() {
            override fun onActivityResumed(activity: Activity) {
                appCurrentActivity = WeakReference(activity)
                pageChangeListeners.forEach { it.onPageShow() }
            }
        })

        mConfig.autoOpenMonitors.forEach {
            GiraffeSettings.setAutoOpenFlag(application, it, true)
        }

        monitorMap.values.forEach {
            val autoOpen = GiraffeSettings.autoOpen(application, it.getMonitorInfo().name)
            if (autoOpen) {
                it.open(application)
                mConfig.autoOpenMonitors.add(it.getMonitorInfo().name)
                GiraffeLog.d(TAG_MONITOR, "monitor auto open : ${it.getMonitorInfo().name} ")
            }
        }

        isInit = true

        GiraffeLog.d("GiraffeMonitor init success!!")
    }

    fun saveCrash(e: Throwable, thread: Thread) {
        getMonitor<GiraffeExceptionMonitor>()?.saveCrash(e, thread)
    }

    fun getCurrentPage() = appCurrentActivity?.get()?.javaClass?.name ?: ""

    private inline fun <reified T : GiraffeMonitorProtocol> getMonitor(): T? {
        for (monitor in monitorMap.values) {
            if (monitor is T) {
                return monitor
            }
        }
        return null
    }

//    fun monitorRequest(requestUrl: String): Boolean {
//        val appSpeedMonitor = monitorMap[GiraffeMonitorProtocol.APP_SPEED.name]
//        if (appSpeedMonitor is RabbitAppSpeedMonitor) {
//            return appSpeedMonitor.monitorRequest(requestUrl)
//        }
//        return false
//    }

//    fun markRequestFinish(requestUrl: String, costTime: Long) {
//        val appSpeedMonitor = monitorMap[RabbitMonitorProtocol.APP_SPEED.name]
//        if (appSpeedMonitor is RabbitAppSpeedMonitor) {
//            appSpeedMonitor.markRequestFinish(requestUrl, costTime)
//        }
//    }

    fun getNetMonitor(): Interceptor {
        return getMonitor<GiraffeNetMonitor>() ?: GiraffeNetMonitor()
    }

    interface PageChangeListener {
        fun onPageShow()
    }

}