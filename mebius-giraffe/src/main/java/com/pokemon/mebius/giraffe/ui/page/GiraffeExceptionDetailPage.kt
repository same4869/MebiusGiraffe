package com.pokemon.mebius.giraffe.ui.page

import android.content.Context
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import com.pokemon.mebius.giraffe.R
import com.pokemon.mebius.giraffe.base.entities.GiraffeExceptionInfo
import com.pokemon.mebius.giraffe.ui.utils.GiraffeUiUtils
import kotlinx.android.synthetic.main.giraffe_page_call_stack.view.mCallStackThreadName
import kotlinx.android.synthetic.main.giraffe_page_call_stack.view.mCallStackTvStackStr

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/6/9
 */

class GiraffeExceptionDetailPage(context: Context) : GiraffeBasePage(context) {

    override fun getLayoutResId() = R.layout.giraffe_page_call_stack

    init {
        setTitle(context.resources.getString(R.string.giraffe_exception_title))
    }

    override fun setEntryParams(exceptionInfo: Any) {
        if (exceptionInfo !is GiraffeExceptionInfo) return

        if (!exceptionInfo.isvalid()) {
            showToast(context.resources.getString(R.string.giraffe_logfile_exception))
            return
        }

        mCallStackThreadName.text = "Crash Thread Name : ${exceptionInfo?.threadName ?: ""}"

        val redMaxLine = 4
        val spannableStringBuilder = SpannableStringBuilder()
        spannableStringBuilder.append(context.resources.getString(R.string.giraffe_copy_tips))

        var spanEndIndex = 0

        exceptionInfo.crashTraceStr.split("\n").forEachIndexed { index, string ->
            spannableStringBuilder.append(string)
            spannableStringBuilder.append("\n")
            if (index < redMaxLine) {
                spanEndIndex = spannableStringBuilder.length
            }
        }

        val foregroundColorSpan = ForegroundColorSpan(Color.RED)
        spannableStringBuilder.setSpan(
            foregroundColorSpan,
            0,
            spanEndIndex,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )

        mCallStackTvStackStr.text = spannableStringBuilder

        mCallStackTvStackStr.setOnLongClickListener {
            GiraffeUiUtils.copyStrToClipBoard(context, mCallStackTvStackStr.text.toString())
            showToast(context.resources.getString(R.string.giraffe_copy_tips1))
            true
        }
    }

}