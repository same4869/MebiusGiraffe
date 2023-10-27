package com.pokemon.mebius.giraffe.ui

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.pokemon.mebius.giraffe.R
import com.pokemon.mebius.giraffe.base.common.GiraffeActivityLifecycleWrapper
import com.pokemon.mebius.giraffe.ui.view.GiraffeFloatingToastView
import com.pokemon.mebius.giraffe.ui.view.GiraffeFloatingView
import java.lang.ref.WeakReference

object GiraffeUiKernel {
    const val PAGE_NULL = 1
    const val PAGE_HIDE = 2
    const val PAGE_SHOWING = 3

    lateinit var application: Application

    //页面是否在展示
    private var pageShowStatus = PAGE_NULL

    //悬浮的View是否在显示
    private var floatingViewIsShow = false

    private var mEntryPage: GiraffePageProtocol? = null

    //当前应用正在展示的Activity
    var appCurrentActivity: WeakReference<Activity?>? = null

    //rabbit floating views
    private val floatingView by lazy {
        GiraffeFloatingView(application)
    }

    /**
     * 悬浮层上的toast实现
     */
    private val floatingToastView by lazy {
        GiraffeFloatingToastView(application)
    }

    //应用可见性监控
    private val applicationLifecycle by lazy {
        object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            fun onForeground() {
                showFloatingView()
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onBackground() {
                hideFloatingView()
                hideAllPage()
            }
        }
    }

    private val pageList = ArrayList<GiraffePageProtocol>()
    private val pageContainer by lazy {
        FrameLayout(application).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            background =
                getDrawable(context, R.color.giraffe_transparent_black)
        }
    }

    fun init(application_: Application, entryPage: GiraffePageProtocol?) {
        application = application_
        mEntryPage = entryPage
        application.registerActivityLifecycleCallbacks(object :
            GiraffeActivityLifecycleWrapper() {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                appCurrentActivity = WeakReference(activity)
            }

            override fun onActivityResumed(activity: Activity) {
                appCurrentActivity = WeakReference(activity)
            }
        })
    }

    fun openPage(pageClass: Class<out View>?, params: Any? = null) {
        if (pageClass == null) return
        val pageProtocol = addPage(pageClass) ?: return
        if (params != null) {
            pageProtocol.setEntryParams(params)
        }
        pushPageToTopLevel(pageProtocol)
    }

    private fun addPage(pageClass: Class<out View>): GiraffePageProtocol? {
        var newedView: View? = null

//        for (surInt in pageClass.interfaces) {
//            if (surInt == GiraffePageProtocol::class.java) {
//                newedView = pageClass.getConstructor(Context::class.java).newInstance(application)
//                break
//            }
//        }
//
//        if (newedView == null) {
//            for (surInt in pageClass.superclass!!.interfaces) {
//                if (surInt == GiraffePageProtocol::class.java) {
//                    newedView = pageClass.getConstructor(Context::class.java)
//                        .newInstance(application)
//                    break
//                }
//            }
//        }
        try {
            newedView = pageClass.getConstructor(Context::class.java).newInstance(application)
        } catch (e: Throwable) {
            e.printStackTrace()
            return null
        }

        if (newedView == null || newedView !is GiraffePageProtocol) {
            return null
        }

        pageList.add(newedView)

        return newedView
    }

    fun handleFloatingViewClickEvent() {
        when (pageShowStatus) {
            PAGE_NULL -> showGiraffeEntryPage()
            PAGE_SHOWING -> hideGiraffePage()
            PAGE_HIDE -> restoreGiraffePage()
        }
    }

    private fun restoreGiraffePage() {
        pageShowStatus =
            PAGE_SHOWING
        pageContainer.visibility = View.VISIBLE
    }

    private fun showGiraffeEntryPage() {
        hideFloatingView()
        if (mEntryPage == null || mEntryPage !is View) return
        getWm().addView(
            pageContainer,
            getPageParams()
        )
        pushPageToTopLevel(mEntryPage!!)
        showFloatingView()
    }

    private fun getPageParams(): WindowManager.LayoutParams {
        return WindowManager.LayoutParams().apply {
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR
            }
            flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
            format = PixelFormat.RGBA_8888
            gravity = Gravity.START or Gravity.TOP
            height = WindowManager.LayoutParams.MATCH_PARENT
            width = WindowManager.LayoutParams.MATCH_PARENT
            x = 0
            y = 0
            windowAnimations = android.R.style.Animation_Toast
        }
    }

    private fun pushPageToTopLevel(newPage: GiraffePageProtocol) {
        pageList.add(newPage)

        if (newPage is View) {

            setBackEventListener(newPage)

            val layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            newPage.layoutParams = layoutParams

            newPage.setOnApplyWindowInsetsListener { v, insets ->
                layoutParams.topMargin = insets.stableInsetTop
                layoutParams.bottomMargin = insets.stableInsetBottom
                layoutParams.leftMargin = insets.stableInsetLeft
                layoutParams.rightMargin = insets.stableInsetRight
                newPage.layoutParams = layoutParams
                insets
            }
            newPage.fitsSystemWindows = true
            newPage.post {
                newPage.requestApplyInsets()
            }

            pageContainer.addView(newPage)

            pageShowStatus = PAGE_SHOWING
        }
    }

    private fun setBackEventListener(GiraffePage: GiraffePageProtocol) {
        GiraffePage.eventListener = object :
            GiraffePageProtocol.PageEventListener {
            override fun onBack() {
                popPageFromTopLevel()
            }
        }
    }

    private fun getWm() = (application.getSystemService(Context.WINDOW_SERVICE) as WindowManager)

    private fun popPageFromTopLevel() {
        val removedPage = pageList.removeAt(pageList.size - 1)
        pageContainer.removeView(removedPage as View)
        if (pageList.isEmpty()) {
            pageShowStatus =
                PAGE_NULL
            getWm()
                .removeView(pageContainer)
        }
    }

    fun showFloatingView() {
        if (floatingViewIsShow) return
        floatingViewIsShow = true
        floatingView.show()
        floatingToastView.show()
        listenAppLifeCycle()
    }

    fun hideFloatingView() {
        floatingViewIsShow = false
        floatingView.hide()
        floatingToastView.hide()
    }

    fun hideAllPage() {
        if (pageShowStatus != PAGE_SHOWING) return
        hideGiraffePage()
    }

    private fun hideGiraffePage() {
        pageShowStatus = PAGE_HIDE
        pageContainer.visibility = View.GONE
    }

    private fun listenAppLifeCycle() {
        ProcessLifecycleOwner.get()
            .lifecycle.removeObserver(applicationLifecycle)
        ProcessLifecycleOwner.get()
            .lifecycle.addObserver(applicationLifecycle)
    }


    fun pageIsShow() = pageShowStatus == PAGE_SHOWING
}