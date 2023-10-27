package com.pokemon.mebius.giraffe.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.pokemon.mebius.commlib.utils.dp2px
import com.pokemon.mebius.commlib.utils.onClick
import com.pokemon.mebius.giraffe.R
import kotlinx.android.synthetic.main.giraffe_action_bar.view.mDevToolsToolsBarIvBack
import kotlinx.android.synthetic.main.giraffe_action_bar.view.mDevToolsToolsBarIvOperation
import kotlinx.android.synthetic.main.giraffe_action_bar.view.mDevToolsToolsBarTvTitle

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/6/8
 */

class GiraffeActionBar : RelativeLayout {
    var actionListener: ActionListener? = null

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initView()
    }

    private fun initView() {
        LayoutInflater.from(context).inflate(R.layout.giraffe_action_bar, this)
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            50.dp2px
        )

        background = getDrawable(context, R.color.giraffe_material_primary)

        mDevToolsToolsBarIvBack.onClick {
            actionListener?.onBackClick()
        }

//        mGiraffeActionBarFakeEt.inputType = InputType.TYPE_NULL
//        mGiraffeActionBarFakeEt.setOnKeyListener(OnKeyListener { v, keyCode, event ->
//            if (event?.keyCode == KeyEvent.KEYCODE_BACK && GiraffeUiKernel.pageIsShow()) {
//                actionListener?.onBackClick()
//                return@OnKeyListener true
//            } else {
//                return@OnKeyListener false
//            }
//        })
    }

    fun setTitle(title: String) {
        mDevToolsToolsBarTvTitle.text = title
    }

    fun setRightOperate(draRes: Int, clickCallback: () -> Unit) {
        mDevToolsToolsBarIvOperation.visibility = View.VISIBLE
        mDevToolsToolsBarIvOperation.setImageDrawable(getDrawable(context, draRes))
        mDevToolsToolsBarIvOperation.setOnClickListener {
            clickCallback()
        }
    }

    interface ActionListener {
        fun onBackClick()
    }

    //support back by back keyword
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
//        postDelayed({
//            if (!mGiraffeActionBarFakeEt.hasFocus()) {
//                mGiraffeActionBarFakeEt.requestFocus()
//            }
//        }, 500)
    }
}