package com.pokemon.mebius.giraffe.ui

import android.app.Application
import com.pokemon.mebius.giraffe.base.GiraffeLog
import com.pokemon.mebius.giraffe.config.GiraffeUiConfig

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
        GiraffeLog.d("WolfUi init success!!")
    }
}