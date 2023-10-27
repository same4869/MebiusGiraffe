package com.pokemon.mebius.giraffe.config

import android.view.View
import androidx.annotation.DrawableRes

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/6/8
 */

class GiraffeMainFeatureInfo(
    val name: String,
    @DrawableRes val icon: Int,
    val pageClass: Class<out View>?,
    val action: () -> Unit = {}
)