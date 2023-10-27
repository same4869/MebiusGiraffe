package com.pokemon.mebius.giraffe

import android.app.Activity
import android.app.Application
import android.content.Context
import android.view.View
import com.pokemon.mebius.giraffe.base.GiraffeLog
import com.pokemon.mebius.giraffe.base.GiraffeProtocol
import com.pokemon.mebius.giraffe.base.common.GiraffeUtils
import com.pokemon.mebius.giraffe.config.GiraffeConfig
import com.pokemon.mebius.giraffe.ui.GiraffeUi
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
        TODO("Not yet implemented")
    }

    override fun getNetInterceptor(): Interceptor {
        TODO("Not yet implemented")
    }

    override fun open(requestPermission: Boolean, activity: Activity) {
        TODO("Not yet implemented")
    }

    override fun changeAutoOpenStatus(context: Context, autoOpen: Boolean) {
        TODO("Not yet implemented")
    }

    override fun isAutoOpen(context: Context): Boolean {
        TODO("Not yet implemented")
    }

    override fun getCurrentActivity(): Activity? {
        TODO("Not yet implemented")
    }

    override fun openPage(pageClass: Class<out View>?, params: Any?) {
        TODO("Not yet implemented")
    }

    private fun configUi() {
        val uiConfig = mConfig.uiConfig
        uiConfig.entryFeatures.addAll(GiraffeUi.defaultSupportFeatures(application))
    }
}