package com.pokemon.mebius.giraffe.ui

import android.app.Application
import android.view.View
import com.pokemon.mebius.commlib.utils.APPLICATION
import com.pokemon.mebius.giraffe.R
import com.pokemon.mebius.giraffe.base.GiraffeLog
import com.pokemon.mebius.giraffe.base.ui.GiraffeEntryPage
import com.pokemon.mebius.giraffe.config.GiraffeMainFeatureInfo
import com.pokemon.mebius.giraffe.config.GiraffeUiConfig
import com.pokemon.mebius.giraffe.ui.page.GiraffeExceptionListPage
import com.pokemon.mebius.giraffe.ui.page.GiraffeHttpLogListPage
import com.pokemon.mebius.giraffe.ui.page.GiraffeQuickFunctionPage
import com.pokemon.mebius.giraffe.ui.page.GiraffeToastHistoryPage

object GiraffeUi {
    var mConfig: GiraffeUiConfig = GiraffeUiConfig()

    fun init(application: Application, config: GiraffeUiConfig) {
        mConfig = config
        GiraffeUiKernel.init(
            application,
            GiraffeEntryPage(application, mConfig.entryFeatures, rightOpeClickCallback = {
                GiraffeUiKernel.openPage(GiraffeQuickFunctionPage::class.java)
            })
        )
        GiraffeLog.d("GiraffeUi init success!!")
    }

    fun defaultSupportFeatures(application: Application): ArrayList<GiraffeMainFeatureInfo> {
        return ArrayList<GiraffeMainFeatureInfo>().apply {
            add(
                GiraffeMainFeatureInfo(
                    APPLICATION.resources.getString(R.string.giraffe_net_title),
                    R.drawable.giraffe_icon_http,
                    GiraffeHttpLogListPage::class.java
                )
            )
            add(
                GiraffeMainFeatureInfo(
                    APPLICATION.resources.getString(R.string.giraffe_exception_title3),
                    R.drawable.giraffe_icon_exception,
                    GiraffeExceptionListPage::class.java
                )
            )
            add(
                GiraffeMainFeatureInfo(
                    APPLICATION.resources.getString(R.string.giraffe_toast_history_title),
                    R.drawable.ic_twotone_announcement_24,
                    GiraffeToastHistoryPage::class.java
                )
            )
        }
    }

    fun openPage(pageClass: Class<out View>?, params: Any? = null) =
        GiraffeUiKernel.openPage(pageClass, params)

    fun getCurrentActivity() = GiraffeUiKernel.appCurrentActivity?.get()
}