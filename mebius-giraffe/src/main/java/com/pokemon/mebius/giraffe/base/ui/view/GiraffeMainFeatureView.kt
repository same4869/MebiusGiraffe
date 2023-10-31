package com.pokemon.mebius.giraffe.base.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.pokemon.mebius.commlib.utils.dp2px
import com.pokemon.mebius.commlib.utils.getDrawable
import com.pokemon.mebius.commlib.utils.onClick
import com.pokemon.mebius.giraffe.R
import com.pokemon.mebius.giraffe.config.GiraffeMainFeatureInfo
import com.pokemon.mebius.giraffe.ui.GiraffeUiKernel
import com.pokemon.mebius.mebius_widget_recycleview.interf.AdapterItemView
import kotlinx.android.synthetic.main.giraffe_view_main_feature_view.view.mDevToolsMainFeatureIvIcon
import kotlinx.android.synthetic.main.giraffe_view_main_feature_view.view.mDevToolsMainFeatureTvName

class GiraffeMainFeatureView  : LinearLayout, AdapterItemView<GiraffeMainFeatureInfo> {

    var clickListener: ClickListener? = null
    lateinit var mFeatureInfo: GiraffeMainFeatureInfo

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initView()
    }

    private fun initView() {
        LayoutInflater.from(context).inflate(R.layout.giraffe_view_main_feature_view, this)
        layoutParams = MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        orientation = HORIZONTAL
        setPadding(13.dp2px, 20.dp2px, 10.dp2px, 20.dp2px)
        background = getDrawable(context, R.color.giraffe_bg_card)
        onClick {
            clickListener?.onClick()
            GiraffeUiKernel.openPage(mFeatureInfo.pageClass)
            if (mFeatureInfo.pageClass == null) {
                mFeatureInfo.action()
            }
        }
    }

    override fun bindData(fearureInfo: GiraffeMainFeatureInfo, position: Int) {
        mFeatureInfo = fearureInfo
        mDevToolsMainFeatureTvName.text = fearureInfo.name
        mDevToolsMainFeatureIvIcon.setImageDrawable(
            getDrawable(
                context,
                fearureInfo.icon
            )
        )
    }

    interface ClickListener {
        fun onClick()
    }

}