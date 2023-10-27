package com.pokemon.mebius.giraffe.ui.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import com.pokemon.mebius.giraffe.R
import com.pokemon.mebius.giraffe.ui.GiraffeUiKernel
import com.pokemon.mebius.giraffe.ui.utils.GiraffeUiUtils
import kotlinx.android.synthetic.main.giraffe_view_floating.view.mDevToolsFloatingIv
import kotlinx.android.synthetic.main.giraffe_view_floating.view.mGiraffeFloatingIvParent
import kotlin.math.abs

class GiraffeFloatingView(context: Context) : LinearLayout(context) {
    private var isShow = false
    private var mXInScreen: Float = 0.toFloat()
    private var mYInScreen: Float = 0.toFloat()

    private val mParams: WindowManager.LayoutParams by lazy {
        WindowManager.LayoutParams()
    }

    private val mWindowManager: WindowManager by lazy {
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.giraffe_view_floating, this)
        orientation = VERTICAL
        layoutParams =
            LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        isClickable = true

        mDevToolsFloatingIv.setOnClickListener {
            GiraffeUiKernel.handleFloatingViewClickEvent()
        }
    }

    fun show() {
        if (isShow) return

        isShow = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR
        }

        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        mParams.format = PixelFormat.RGBA_8888
        mParams.gravity = Gravity.START or Gravity.TOP
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        mParams.x = 0
        mParams.y = GiraffeUiUtils.getScreenHeight() / 3
        mParams.windowAnimations = android.R.style.Animation_Toast
        try {
            mWindowManager.addView(this, mParams)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hide() {
        isShow = false
        mWindowManager.removeView(this)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mXInScreen = event.rawX
                mYInScreen = event.rawY
            }

            MotionEvent.ACTION_MOVE -> {
                mXInScreen = event.rawX
                mYInScreen = event.rawY
                updateViewPosition()
            }

            MotionEvent.ACTION_UP -> {
                moveToEdge()
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun updateViewPosition() {
        mParams.x = (mXInScreen - width).toInt()
        mParams.y = (mYInScreen - height).toInt()
        mWindowManager.updateViewLayout(this, mParams)
    }

    private fun changeFlowingViewGravity(newGravity: Int) {
        (mGiraffeFloatingIvParent.layoutParams as LayoutParams).gravity = newGravity
    }

    private fun moveToEdge() {
        val start = mXInScreen
        val screenWidth = mWindowManager.defaultDisplay?.width
        val end: Float
        end = if (mXInScreen > (screenWidth ?: 0) / 2) {
            changeFlowingViewGravity(Gravity.END)
            (screenWidth ?: 0).toFloat()
        } else {
            changeFlowingViewGravity(Gravity.START)
            0f
        }
        val time = abs(start - end).toLong() * 800 / (screenWidth ?: 0)
        val animator = ValueAnimator.ofFloat(start, end).setDuration(time)
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener { animation ->
            try {
                val value = animation.animatedValue as Float
                mParams.x = value.toInt()
                mWindowManager.updateViewLayout(this@GiraffeFloatingView, mParams)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        animator.start()
    }
}