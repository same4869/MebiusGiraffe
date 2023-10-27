package com.pokemon.mebius.giraffe

import android.app.Activity
import android.app.Application
import android.content.Context
import android.view.View
import com.pokemon.mebius.giraffe.base.GiraffeLog
import com.pokemon.mebius.giraffe.base.GiraffeProtocol
import com.pokemon.mebius.giraffe.base.GiraffeSettings
import com.pokemon.mebius.giraffe.base.common.GiraffeUtils
import com.pokemon.mebius.giraffe.config.GiraffeConfig
import com.pokemon.mebius.giraffe.monitor.GiraffeMonitor
import com.pokemon.mebius.giraffe.storage.GiraffeStorage
import com.pokemon.mebius.giraffe.ui.GiraffeUi
import com.pokemon.mebius.giraffe.ui.GiraffeUiKernel
import com.pokemon.mebius.giraffe.ui.utils.FloatingViewPermissionHelper
import okhttp3.Interceptor

object Giraffe : GiraffeProtocol {
    private var mConfig = GiraffeConfig()
    lateinit var application: Application
    private var _isInit = false

    val isInit get() = _isInit
    override fun init(applicationP: Application, config: GiraffeConfig) {
        if (!GiraffeUtils.isMainProcess(applicationP)) return

        application = applicationP
        mConfig = config

        if (!mConfig.enable || _isInit) {
            return
        }

        GiraffeLog.isEnable = mConfig.enableLog

        configUi()
        initAllComponent()

        _isInit = true

        GiraffeLog.d("init success!!")
    }

    override fun saveCrashLog(e: Throwable) {
        GiraffeMonitor.saveCrash(e, Thread.currentThread())
    }

    override fun getNetInterceptor():  Interceptor = GiraffeMonitor.getNetMonitor()

    override fun open(requestPermission: Boolean, activity: Activity) {
        val overlayPermissionIsOpen = FloatingViewPermissionHelper.checkPermission(application)

        if (!requestPermission && !overlayPermissionIsOpen) return

        if (overlayPermissionIsOpen) {
            GiraffeUiKernel.showFloatingView()
        } else {
            FloatingViewPermissionHelper.showConfirmDialog(
                activity,
                object : FloatingViewPermissionHelper.OnConfirmResult {
                    override fun confirmResult(confirm: Boolean) {
                        if (confirm) {
                            FloatingViewPermissionHelper.requestFloatingWindowPermission(
                                application
                            )
                        }
                    }
                })
        }
    }

    override fun changeAutoOpenStatus(context: Context, autoOpen: Boolean) {
        GiraffeSettings.autoOpenGiraffe(context, autoOpen)
    }

    override fun isAutoOpen(context: Context): Boolean  = GiraffeSettings.autoOpenGiraffe(context)

    override fun getCurrentActivity() = GiraffeUi.getCurrentActivity()

    override fun openPage(pageClass: Class<out View>?, params: Any?) {
        GiraffeUi.openPage(pageClass, params)
    }

    private fun initAllComponent() {
        GiraffeUi.init(application, mConfig.uiConfig)
        GiraffeStorage.init(application, mConfig.storageConfig)
        GiraffeMonitor.init(application, mConfig.monitorConfig)
    }

    private fun configUi() {
        val uiConfig = mConfig.uiConfig
        uiConfig.entryFeatures.addAll(GiraffeUi.defaultSupportFeatures(application))
    }
}