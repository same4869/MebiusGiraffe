package com.pokemon.mebius.giraffe.ui.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.pokemon.mebius.commlib.utils.dp2px
import com.pokemon.mebius.commlib.utils.getDrawable
import com.pokemon.mebius.commlib.utils.onClick
import com.pokemon.mebius.giraffe.R
import com.pokemon.mebius.giraffe.base.common.giraffeTimeFormat
import com.pokemon.mebius.giraffe.base.entities.GiraffeExceptionInfo
import com.pokemon.mebius.giraffe.ui.GiraffeUi
import com.pokemon.mebius.giraffe.ui.page.GiraffeExceptionDetailPage
import com.pokemon.mebius.mebius_widget_recycleview.interf.AdapterItemView
import kotlinx.android.synthetic.main.giraffe_view_exception_log_pre_view_item.view.mExceptionPreviewIv
import kotlinx.android.synthetic.main.giraffe_view_exception_log_pre_view_item.view.mExceptionPreviewTvLine1
import kotlinx.android.synthetic.main.giraffe_view_exception_log_pre_view_item.view.mExceptionPreviewTvLine2

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/6/9
 */

class GiraffeExceptionPreviewView(context: Context) : RelativeLayout(context),
    AdapterItemView<GiraffeExceptionInfo> {

    private lateinit var mLogInfo: GiraffeExceptionInfo

    init {
        LayoutInflater.from(context).inflate(R.layout.giraffe_view_exception_log_pre_view_item, this)
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            50.dp2px
        ).apply {
            bottomMargin = 5.dp2px
        }
        onClick {
            GiraffeUi.openPage(GiraffeExceptionDetailPage::class.java, mLogInfo)
        }
    }

    override fun bindData(info: GiraffeExceptionInfo, position: Int) {
        mLogInfo = info
        mExceptionPreviewIv.setImageDrawable(
            getDrawable(
                context,
                R.drawable.giraffe_icon_exception
            )
        )

        val simpleExceptionName = info.exceptionName.substring(
            info.exceptionName.lastIndexOf('.') + 1,
            info.exceptionName.length
        )
        mExceptionPreviewTvLine1.text = "${simpleExceptionName}  ${giraffeTimeFormat(
            info.time
        )}"
        if (info.simpleMessage.isNotEmpty()) {
            mExceptionPreviewTvLine2.visibility = View.VISIBLE
            mExceptionPreviewTvLine2.text = info.simpleMessage
        } else {
            mExceptionPreviewTvLine2.visibility = View.GONE
        }
    }
}