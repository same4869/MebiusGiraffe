package com.pokemon.mebius.giraffe.base

import android.util.Log

const val TAG_COMMON = "giraffe-log"
const val TAG_STORAGE = "giraffe-storage-log"
const val TAG_MONITOR = "giraffe-monitor"
const val TAG_UI = "giraffe-ui-log"
const val TAG_MONITOR_UI = "giraffe-monitor-ui-log"

object GiraffeLog {
    var isEnable: Boolean = true

    @JvmStatic
    fun d(tag: String, logStr: String) {
        if (isEnable) {
            Log.d(tag, logStr)
        }
    }

    @JvmStatic
    fun d(tag: String, suffix: String, logStr: String) {
        if (isEnable) {
            Log.d("$tag$suffix", logStr)
        }
    }

    @JvmStatic
    fun d(logStr: String) {
        if (isEnable) {
            Log.d(TAG_COMMON, logStr)
        }
    }

    @JvmStatic
    fun e(tag: String, logStr: String) {
        if (isEnable) {
            Log.e(tag, logStr)
        }
    }

    @JvmStatic
    fun e(logStr: String) {
        if (isEnable) {
            Log.e(TAG_COMMON, logStr)
        }
    }

    fun getStackTraceString(e: Throwable): String = Log.getStackTraceString(e)
}