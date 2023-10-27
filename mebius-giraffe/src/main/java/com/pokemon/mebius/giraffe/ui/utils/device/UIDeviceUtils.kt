package com.pokemon.mebius.giraffe.ui.utils.device

import android.text.TextUtils
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object UIDeviceUtils {

    /**
     * checking if is meizu rom
     */
    fun checkIsMeizuRom(): Boolean {
        val meizuFlymeOSFlag = getSystemProperty("ro.build.display.id")
        return if (TextUtils.isEmpty(meizuFlymeOSFlag)) {
            false
        } else meizuFlymeOSFlag!!.contains("flyme") || meizuFlymeOSFlag.toLowerCase().contains("flyme")
    }

    /**
     * get system property for "ro.build.version.emui"
     */
    fun getSystemProperty(propName: String): String? {
        val line: String
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $propName")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            line = input.readLine()
            input.close()
        } catch (ex: IOException) {
            return null
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {

                }
            }
        }
        return line
    }
}