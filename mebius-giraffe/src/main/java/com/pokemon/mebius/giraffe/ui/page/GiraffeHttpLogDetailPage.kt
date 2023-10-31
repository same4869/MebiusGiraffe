package com.pokemon.mebius.giraffe.ui.page

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.google.gson.reflect.TypeToken
import com.pokemon.mebius.commlib.utils.CommJsonParser
import com.pokemon.mebius.giraffe.R
import com.pokemon.mebius.giraffe.base.GiraffeLog
import com.pokemon.mebius.giraffe.base.TAG_COMMON
import com.pokemon.mebius.giraffe.base.TAG_MONITOR_UI
import com.pokemon.mebius.giraffe.base.entities.GiraffeHttpLogInfo
import com.pokemon.mebius.giraffe.ui.GiraffeUiKernel
import com.pokemon.mebius.giraffe.ui.utils.GiraffeUiUtils
import com.pokemon.mebius.giraffe.ui.view.GiraffeHttpDetailItemPreviewView
import com.pokemon.mebius.giraffe.ui.view.GiraffeToast
import kotlinx.android.synthetic.main.giraffe_page_http_log_detail.view.curlCopy
import kotlinx.android.synthetic.main.giraffe_page_http_log_detail.view.httpLogMethod
import kotlinx.android.synthetic.main.giraffe_page_http_log_detail.view.httpLogRequestParams
import kotlinx.android.synthetic.main.giraffe_page_http_log_detail.view.httpLogResponseResultBody
import kotlinx.android.synthetic.main.giraffe_page_http_log_detail.view.httpLogResult
import kotlinx.android.synthetic.main.giraffe_page_http_log_detail.view.httpLogTime
import kotlinx.android.synthetic.main.giraffe_page_http_log_detail.view.httpLogTookTime
import kotlinx.android.synthetic.main.giraffe_page_http_log_detail.view.httpLogUrl
import kotlinx.android.synthetic.main.giraffe_page_http_log_detail.view.mRequestLayout
import kotlinx.android.synthetic.main.giraffe_page_http_log_detail.view.requestHeaderCopy
import kotlinx.android.synthetic.main.giraffe_page_http_log_detail.view.requestParamsCopy
import kotlinx.android.synthetic.main.giraffe_page_http_log_detail.view.responseCopy
import kotlinx.android.synthetic.main.giraffe_page_http_log_detail.view.responseHeaderCopy
import kotlinx.android.synthetic.main.giraffe_page_http_log_detail.view.responseLayout
import kotlinx.android.synthetic.main.giraffe_page_http_log_detail.view.tv_curl
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/6/9
 */

class GiraffeHttpLogDetailPage(context: Context) : GiraffeBasePage(context) {

    override fun getLayoutResId() = R.layout.giraffe_page_http_log_detail

    init {
        setTitle(resources.getString(R.string.giraffe_log_detail_title))
    }

    @SuppressLint("SetTextI18n")
    override fun setEntryParams(logInfo: Any) {

        if (logInfo !is GiraffeHttpLogInfo) return

        if (!logInfo.isvalid()) {
            showToast(resources.getString(R.string.giraffe_logfile_exception))
            return
        }

        GiraffeLog.d(TAG_MONITOR_UI, "enter GiraffeHttpLogDetailPage show")

        httpLogUrl.setupData(
            "request Url",
            "${logInfo.host}${logInfo.path}"
        )

        httpLogMethod.setupData(
            "request method",
            logInfo.requestType
        )

        httpLogResult.setupData(
            "response code",
            if (logInfo.isSuccessRequest) "200" else logInfo.responseCode
        )

        httpLogTime.setupData(
            "request time",
            getTime(logInfo.time)
        )

        httpLogTookTime.setupData(
            "request Total tookTime",
            "${logInfo.tookTime}ms"
        )

        if (logInfo.requestParamsMapString.isNotEmpty()) {
            httpLogRequestParams.visibility = View.VISIBLE
            httpLogRequestParams.bindJson(logInfo.requestParamsMapString)
        }

        if (logInfo.requestBody.isNotEmpty()) {
            httpLogRequestParams.visibility = View.VISIBLE
            httpLogRequestParams.bindJson(logInfo.requestBody)
        }

        if (logInfo.requestBody.isNullOrBlank() && logInfo.requestParamsMapString.isNullOrBlank()) {
            httpLogRequestParams.visibility = View.GONE
        }

        val requestHeaders = CommJsonParser.getCommJsonParser().fromJson<Map<String, List<String>>>(
            logInfo.requestHeaders,
            object : TypeToken<Map<String, List<String>>>() {}.type
        )
        requestHeaders.forEach {
            val headerItemView = GiraffeHttpDetailItemPreviewView(context)
            headerItemView.setupData(
                it.key,
                it.value.toString()
            )
            mRequestLayout.addView(
                headerItemView, LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            )
        }

        if (logInfo.responseStr.isNotEmpty()) {
            httpLogRequestParams.visibility = View.VISIBLE
            httpLogResponseResultBody.bindJson(logInfo.responseStr)
        }

        try {
            CommJsonParser.getCommJsonParser().fromJson<Map<String, List<String>>>(
                logInfo.responseHeaders,
                object : TypeToken<Map<String, List<String>>>() {}.type
            ).forEach {
                val headerItemView = GiraffeHttpDetailItemPreviewView(context)
                headerItemView.setupData(
                    it.key,
                    it.value.toString()
                )
                responseLayout.addView(
                    headerItemView,
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                )
            }

        } catch (e: Exception) {
        }


        try {
            val curlBeanLength = logInfo.curlBean?.length
            if (curlBeanLength != null) {
                tv_curl.text = logInfo.curlBean
            }
        } catch (e: Exception) {
            GiraffeLog.e(
                TAG_COMMON,
                "GiraffeHttpLogDetailPage error $e"
            )
        }

        setupCopy(logInfo)

    }

    /**
     * 设置复制
     */
    private fun setupCopy(logInfo: GiraffeHttpLogInfo) {
        // 复制请求url
        httpLogUrl.setOnLongClickListener {
            GiraffeUiUtils.copyStrToClipBoard(
                context, "${logInfo.host}${logInfo.path}"
            )
            // 因为giraffe弹框会阻拦到toast的显示，所以先隐藏起来
            GiraffeUiKernel.hideAllPage()
            GiraffeToast.showToast(resources.getString(R.string.giraffe_copy_tips1))
            true
        }

        // 复制请求参数
        requestParamsCopy.setOnClickListener {
            val str = if (logInfo.requestParamsMapString.isNotBlank()) {
                logInfo.requestParamsMapString
            } else {
                logInfo.requestBody
            }
            GiraffeUiUtils.copyStrToClipBoard(
                context, str
            )
            // 因为giraffe弹框会阻拦到toast的显示，所以先隐藏起来
            GiraffeUiKernel.hideAllPage()
            GiraffeToast.showToast(resources.getString(R.string.giraffe_copy_tips1))
        }
        // 复制请求参数
        requestHeaderCopy.setOnClickListener {
            GiraffeUiUtils.copyStrToClipBoard(
                context, logInfo.requestHeaders ?: ""
            )
            // 因为giraffe弹框会阻拦到toast的显示，所以先隐藏起来
            GiraffeUiKernel.hideAllPage()
            GiraffeToast.showToast(resources.getString(R.string.giraffe_copy_tips1))
        }
        // 复制请求参数
        responseCopy.setOnClickListener {
            GiraffeUiUtils.copyStrToClipBoard(
                context, logInfo.responseStr
            )
            // 因为giraffe弹框会阻拦到toast的显示，所以先隐藏起来
            GiraffeUiKernel.hideAllPage()
            GiraffeToast.showToast(resources.getString(R.string.giraffe_copy_tips1))
        }
        // 复制请求参数
        responseHeaderCopy.setOnClickListener {
            GiraffeUiUtils.copyStrToClipBoard(
                context, logInfo.responseHeaders ?: ""
            )
            // 因为giraffe弹框会阻拦到toast的显示，所以先隐藏起来
            GiraffeUiKernel.hideAllPage()
            GiraffeToast.showToast(resources.getString(R.string.giraffe_copy_tips1))
        }

        curlCopy.setOnClickListener {
            GiraffeUiUtils.copyStrToClipBoard(
                context, logInfo.curlBean ?: ""
            )
            // 因为giraffe弹框会阻拦到toast的显示，所以先隐藏起来
            GiraffeUiKernel.hideAllPage()
            GiraffeToast.showToast(resources.getString(R.string.giraffe_copy_tips1))
        }

    }

    private fun getTime(time: Long): String {
        return SimpleDateFormat("MM/dd HH:mm:ss").format(Date(time))
    }

}
