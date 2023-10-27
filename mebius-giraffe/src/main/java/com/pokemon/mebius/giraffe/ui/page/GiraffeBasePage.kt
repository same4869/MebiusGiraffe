package com.pokemon.mebius.giraffe.ui.page

import android.content.Context
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.pokemon.mebius.commlib.utils.dp2px
import com.pokemon.mebius.commlib.utils.getColor
import com.pokemon.mebius.commlib.utils.getDrawable
import com.pokemon.mebius.giraffe.R
import com.pokemon.mebius.giraffe.ui.GiraffePageProtocol
import com.pokemon.mebius.giraffe.ui.GiraffeUiKernel.application
import com.pokemon.mebius.giraffe.ui.view.GiraffeActionBar

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/6/8
 */

abstract class GiraffeBasePage(context: Context) : FrameLayout(context), GiraffePageProtocol {
    var inflatedView: View? = null

    val ACTION_BAR_HEIGHT = 42.dp2px
    val actionBar = GiraffeActionBar(context).apply {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, ACTION_BAR_HEIGHT)
        actionListener = object : GiraffeActionBar.ActionListener {
            override fun onBackClick() {
                eventListener?.onBack()
            }
        }
    }

    val emptyIv = ImageView(context).apply {
        layoutParams = LinearLayout.LayoutParams(50.dp2px, 50.dp2px).apply {
            gravity = Gravity.CENTER_HORIZONTAL
        }
        setImageDrawable(getDrawable(context, R.drawable.giraffe_icon_empty_data))
    }

    val emptyTv = TextView(context).apply {
        layoutParams =
            LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                gravity = Gravity.CENTER_HORIZONTAL
                topMargin = 10.dp2px
            }
        setTextColor(getColor(context, R.color.giraffe_black))
    }

    private val emptyLl = LinearLayout(context).apply {
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.CENTER
        }
        orientation = LinearLayout.VERTICAL
        addView(emptyIv)
        addView(emptyTv)
    }

    override var eventListener: GiraffePageProtocol.PageEventListener? = null
    val INVALID_RES_ID = -1
    private val tvToast = TextView(context).apply {
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            topMargin = ACTION_BAR_HEIGHT + 20.dp2px
            gravity = Gravity.CENTER_HORIZONTAL
            leftMargin = 20.dp2px
            rightMargin = 20.dp2px
        }
        background = getDrawable(
            context,
            R.drawable.giraffe_bg_toast
        )
        setTextColor(
            getColor(
                context,
                R.color.giraffe_black
            )
        )
        val pd10 = 5.dp2px
        setPadding(pd10, pd10, pd10, pd10)
    }

    init {
        addView(actionBar)
        if (getLayoutResId() != INVALID_RES_ID) {
            inflatedView = LayoutInflater.from(context).inflate(getLayoutResId(), null)
            inflatedView?.layoutParams =
                LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT).apply {
                    topMargin = ACTION_BAR_HEIGHT
                }
            addView(inflatedView)
        }
    }

    fun getContentView(): View? {
        return inflatedView
    }

    fun showToast(msg: String, duration: Long = 1000) {
        tvToast.text = msg
        addView(tvToast)
        postDelayed({
            removeView(tvToast)
        }, duration)
    }

    abstract fun getLayoutResId(): Int

    fun setTitle(title: String) {
        actionBar.setTitle(title)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return true
    }

    fun showEmptyView(msg: String = application.resources.getString(R.string.giraffe_empty_tips)) {
        if (emptyLl.parent == null) {
            addView(emptyLl)
            emptyTv.text = msg
        }
    }

    fun hideEmptyView() {
        if (emptyLl.parent != null) {
            removeView(emptyLl)
        }
    }
}