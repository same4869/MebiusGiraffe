package com.pokemon.mebius.giraffe.ui.utils

import android.content.res.Resources

object GiraffeUiUtils {
    private val sMetrics = Resources.getSystem().displayMetrics

    fun getScreenHeight(): Int {
        return sMetrics?.heightPixels ?: 0
    }
}