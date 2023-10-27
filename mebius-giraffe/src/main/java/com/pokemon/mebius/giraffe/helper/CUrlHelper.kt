package com.pokemon.mebius.giraffe.helper

import android.text.TextUtils
import com.pokemon.mebius.giraffe.base.entities.CurlRequestInfo
import okhttp3.MultipartBody


/**
 * Created by lingyu.wang Wang on 2021/11/1.
 */
class CUrlHelper {

    companion object {
        private const val HEADER = "-H '%1\$s: %2\$s' "
        private const val METHOD = "-X %1\$s "
        private const val BODY_DATA = "-d '%1\$s' "
        private const val URL = "\"%1\$s\""

        public fun convertOKRequestToCUrlInfo(
            request: okhttp3.Request
        ): String {
            val requestInfo = CurlRequestInfo()

            //url
            requestInfo.url = request.url.toString()

            //method
            requestInfo.requestMethod = request.method


            if (request.method != "POST" && request.body != null && request.body!!.contentLength() > 0) return "无CURL"
            if (request.body is MultipartBody) {
                return "【文件上传】无CURL"
            }

            //body data
            val requestBuffer = okio.Buffer()
            request.body?.writeTo(requestBuffer)
            requestInfo.body.data = String(requestBuffer.readByteArray())

            if (requestInfo.body.data.length > 3000) {
                return "CURL内容过长，无法支持"
            }

            val headerInfos = arrayListOf<CurlRequestInfo.Header>()

            val headers = request.headers
            for (i in 0 until headers.size) {
                headerInfos.add(CurlRequestInfo.Header(headers.name(i), headers.value(i)))
            }

            //contentType
            val mediaType = request.body?.contentType()
            if (mediaType != null) {
                requestInfo.body.contentType =
                    if (TextUtils.isEmpty(mediaType.toString())) "" else mediaType.toString()
            } else {
                requestInfo.body.contentType = ""
            }

            if (!TextUtils.isEmpty(requestInfo.body.contentType)) {
                headerInfos.add(CurlRequestInfo.Header("contentType", requestInfo.body.contentType))
            }

            requestInfo.headers = headerInfos


            return convertToCurl(requestInfo)
        }

        private fun convertToCurl(curlRequestInfo: CurlRequestInfo): String {

            val curlStringBuffer = StringBuffer()

            val curlStart = "curl"
            val url = curlRequestInfo.url
            val requestMethod = curlRequestInfo.requestMethod

            if (!TextUtils.isEmpty(requestMethod)) {
                curlStringBuffer.append("$curlStart ")
                    .append(format(METHOD, requestMethod))
            }

            val headers = curlRequestInfo.headers
            for (header in headers) {
                curlStringBuffer.append(format(HEADER, header.key, header.value))
            }

            if (!TextUtils.isEmpty(curlRequestInfo.body.data)) {
                curlStringBuffer.append(format(BODY_DATA, curlRequestInfo.body.data))
            }

            curlStringBuffer.append(format(URL, url))

            return curlStringBuffer.toString()
        }

        private fun format(format: String, vararg args: Any?): String {
            return String.format(format, *args)
        }


    }


}
