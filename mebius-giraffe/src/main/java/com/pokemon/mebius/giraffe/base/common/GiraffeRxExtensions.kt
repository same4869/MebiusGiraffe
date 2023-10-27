package com.pokemon.mebius.giraffe.base.common

import com.pokemon.mebius.giraffe.ui.view.GiraffeToast
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/6/8
 */

fun giraffeTimeFormat(time: Long?): String {
    if (time == null) return ""
    return SimpleDateFormat("MM/dd HH:mm:ss:SSS").format(Date(time))
}

fun giraffeSimpleTimeFormat(time: Long?): String {
    if (time == null) return ""
    return SimpleDateFormat("HH:mm:ss").format(Date(time))
}

fun toastInThread(msg: String) {
    GiraffeToast.showToast(msg)
}