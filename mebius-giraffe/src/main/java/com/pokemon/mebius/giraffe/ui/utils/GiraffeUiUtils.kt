package com.pokemon.mebius.giraffe.ui.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Resources

object GiraffeUiUtils {
    private val sMetrics = Resources.getSystem().displayMetrics

    fun getScreenHeight(): Int {
        return sMetrics?.heightPixels ?: 0
    }

    fun copyStrToClipBoard(context: Context, str: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(null, str)
        clipboard.setPrimaryClip(clipData)
    }
}