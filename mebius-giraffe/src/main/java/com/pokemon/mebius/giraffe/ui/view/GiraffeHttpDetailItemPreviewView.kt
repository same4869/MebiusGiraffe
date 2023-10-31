package com.pokemon.mebius.giraffe.ui.view

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.pokemon.mebius.commlib.utils.dp2px
import com.pokemon.mebius.giraffe.R
import kotlinx.android.synthetic.main.giraffe_view_http_log_detail_pre_view_item.view.mLogDetailPreview

internal class GiraffeHttpDetailItemPreviewView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(
            context
        ).inflate(
            R.layout.giraffe_view_http_log_detail_pre_view_item, this
        )
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            50.dp2px
        ).apply {
            bottomMargin = 5.dp2px
        }
    }


    fun setupData(
        key: String,
        value: String
    ) {
        val keySpan = SpannableStringBuilder(
            key
        )
        ForegroundColorSpan(
            ContextCompat.getColor(context, R.color.giraffe_black)
        ).let {
            keySpan.setSpan(
                it,
                0,
                keySpan.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        val valueSpan = SpannableStringBuilder(
            value
        )
        ForegroundColorSpan(
            ContextCompat.getColor(context, R.color.giraffe_black)
        ).let {
            valueSpan.setSpan(
                it,
                0,
                valueSpan.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        val spanStr = SpannableStringBuilder()
        spanStr.append(keySpan)
        spanStr.append("     ")
        spanStr.append(valueSpan)
        mLogDetailPreview.text = spanStr
    }
}
