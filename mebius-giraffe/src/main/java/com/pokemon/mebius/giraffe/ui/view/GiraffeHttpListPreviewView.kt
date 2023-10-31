package com.pokemon.mebius.giraffe.ui.view

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.pokemon.mebius.commlib.utils.dp2px
import com.pokemon.mebius.commlib.utils.getColor
import com.pokemon.mebius.commlib.utils.getDrawable
import com.pokemon.mebius.commlib.utils.hideKeyboard
import com.pokemon.mebius.commlib.utils.onClick
import com.pokemon.mebius.giraffe.R
import com.pokemon.mebius.giraffe.base.common.giraffeTimeFormat
import com.pokemon.mebius.giraffe.base.entities.GiraffeHttpLogInfo
import com.pokemon.mebius.giraffe.ui.GiraffeUi
import com.pokemon.mebius.giraffe.ui.page.GiraffeHttpLogDetailPage
import com.pokemon.mebius.giraffe.ui.utils.GiraffeUiUtils
import com.pokemon.mebius.mebius_widget_recycleview.interf.AdapterItemView
import kotlinx.android.synthetic.main.giraffe_view_http_log_pre_view_item.view.*


/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/6/8
 */

internal class GiraffeHttpListPreviewView(context: Context) : RelativeLayout(context),
    AdapterItemView<GiraffeHttpLogInfo> {

    private lateinit var mLogInfo: GiraffeHttpLogInfo

    init {
        LayoutInflater.from(context).inflate(R.layout.giraffe_view_http_log_pre_view_item, this)
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            50.dp2px
        ).apply {
            bottomMargin = 5.dp2px
        }
        onClick {
            hideKeyboard()
            GiraffeUi.openPage(GiraffeHttpLogDetailPage::class.java, mLogInfo)
        }

        setOnLongClickListener {

            if (TextUtils.isEmpty(mLogInfo.curlBean)) {
                GiraffeToast.showToast("curl数据异常，无法复制")
                return@setOnLongClickListener true
            }
            Log.d(
                "curltest",
                mLogInfo.curlBean
            )

            GiraffeUiUtils.copyStrToClipBoard(
                context, mLogInfo.curlBean
            )

            GiraffeToast.showToast("复制curl成功，同时可到网络详情页查看具体内容")
            true
        }
    }

    override fun bindData(logInfo: GiraffeHttpLogInfo, position: Int) {
        mLogInfo = logInfo
        mLogPreViewTvHost.text = "${logInfo.host}    ${logInfo.requestType}    ${giraffeTimeFormat(
            logInfo.time
        )} "
        val res = when (logInfo.responseContentType) {
            "gson" -> R.drawable.giraffe_icon_type_json
            else -> R.drawable.giraffe_icon_type_json
        }
        mLogPreViewIvResponseContentType.setImageDrawable(
            getDrawable(
                context,
                res
            )
        )

        if (logInfo.isSuccessRequest) {
            mLogPreViewTvPath.text = logInfo.path
            mLogPreViewTvPath.setTextColor(
                getColor(
                    context,
                    R.color.giraffe_black
                )
            )
            mLogPreViewTvHost.setTextColor(
                getColor(
                    context,
                    R.color.giraffe_black
                )
            )
        } else {
            mLogPreViewTvPath.text = "${logInfo.path} ${logInfo.responseCode}"
            mLogPreViewTvPath.setTextColor(
                getColor(
                    context,
                    R.color.giraffe_error_red
                )
            )
            mLogPreViewTvHost.setTextColor(
                getColor(
                    context,
                    R.color.giraffe_error_red
                )
            )
        }
    }

}
