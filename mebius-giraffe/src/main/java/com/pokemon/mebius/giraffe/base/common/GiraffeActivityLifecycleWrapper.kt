package com.pokemon.mebius.giraffe.base.common

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/6/9
 */

open class GiraffeActivityLifecycleWrapper : Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(p0: Activity) {
    }

    override fun onActivityStarted(p0: Activity) {
    }

    override fun onActivityDestroyed(p0: Activity) {
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
    }

    override fun onActivityStopped(p0: Activity) {
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
    }

    override fun onActivityResumed(p0: Activity) {
    }

}