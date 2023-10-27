package com.pokemon.mebius.giraffe.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.view.View
import com.pokemon.mebius.giraffe.config.GiraffeConfig
import okhttp3.Interceptor

interface GiraffeProtocol {
    fun init(application: Application, config: GiraffeConfig)

    fun saveCrashLog(e: Throwable)

    fun getNetInterceptor(): Interceptor

    fun open(requestPermission: Boolean = true, activity: Activity)

    fun changeAutoOpenStatus(context: Context, autoOpen: Boolean)

    fun isAutoOpen(context: Context): Boolean

    fun getCurrentActivity(): Activity?

    fun openPage(pageClass: Class<out View>?, params: Any? = null)
}