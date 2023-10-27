package com.pokemon.mebius.framework.buildsrc

object Repos {
    const val maven_repo = "http://localhost:8081/repository/maven-releases/"
}

object Dependencies {
    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.agp}"
    const val sora_commlib = "com.pokemon.mebius:commlib:0.0.1"
    const val gson = "com.google.code.gson:gson:2.8.7"
    const val green_dao_plugin = "org.greenrobot:greendao-gradle-plugin:3.3.0"
    const val green_dao = "org.greenrobot:greendao:3.3.0"
    const val rx_java = "io.reactivex.rxjava2:rxjava:2.2.19"
    const val rx_android = "io.reactivex.rxjava2:rxandroid:2.1.1"
    const val okhttp3 = "com.squareup.okhttp3:okhttp:4.8.0"
//    const val sora_widget = "com.mihoyo.framework.sora:widget:0.0.87"
//    const val sora_arch = "com.mihoyo.framework.sora:arch:0.0.1"
}

object SoraWidget {
    const val debug_mode = true
    const val widget = "com.mihoyo.framework.sora:widget:0.0.89"
    const val refresh = "com.mihoyo.framework.sora:widget-refresh:1.0.0"
    const val refresh_old = "com.mihoyo.framework.sora:widget-refresh-old:1.0.0"
    const val guide = "com.mihoyo.framework.sora:widget-guide:1.0.0"
    const val lTab = "com.mihoyo.framework.sora:widget-ltab:1.0.0"
    const val vector = "com.mihoyo.framework.sora:widget-vector:1.0.4"
    const val brackets = "com.mihoyo.framework.sora:widget-brackets:1.0.5"
}

object SoraWolf {
    const val debug_mode = false
    const val core = ""
}

object Kotlin {
    const val gradle_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val std = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
}

object UIKit {
    const val material = "com.google.android.material:material:1.4.0"
    const val constraint_layout = "androidx.constraintlayout:constraintlayout:2.0.4"
    const val recycler_view = "androidx.recyclerview:recyclerview:1.1.0"
    const val swipe_refresh_layout = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"
}

object AndroidX {
    const val core = "androidx.core:core:1.5.0"
    const val core_ktx = "androidx.core:core-ktx:1.3.2"
    const val appcompat = "androidx.appcompat:appcompat:1.2.0"
    const val fragment_ktx = "androidx.fragment:fragment-ktx:1.2.0"
    const val lifecycle_ext = "androidx.lifecycle:lifecycle-extensions:2.2.0"
}

object Versions {
    const val compileSdkVersion = 30
    const val minSdkVersion = 23
    const val targetSdkVersion = 30
    const val kotlin = "1.5.30"
    const val agp = "7.0.3"
}

