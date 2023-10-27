package com.pokemon.mebius.giraffe.monitor.utils

import com.pokemon.mebius.commlib.utils.CommJsonParser
import com.pokemon.mebius.giraffe.base.GiraffeLog
import com.pokemon.mebius.giraffe.base.entities.GiraffeHttpLogInfo
import com.pokemon.mebius.giraffe.helper.CUrlHelper
import okhttp3.*
import okhttp3.internal.http.promisesBody
import okio.Buffer
import java.net.URLDecoder
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/6/9
 */

internal object GiraffeHttpResponseParser {
    fun parserResponse(request: Request, response: Response, startTime: Long): GiraffeHttpLogInfo {
        return when {
            isSuccessResponse(response.code) -> parseSuccessHttpLog(
                response,
                request,
                startTime
            )
            else -> parseErrorHttpLog(
                response,
                request,
                startTime
            )
        }
    }

    private fun parseErrorHttpLog(
        response: Response,
        request: Request,
        startTime: Long
    ): GiraffeHttpLogInfo {
        val logInfo = GiraffeHttpLogInfo()
        val reqHttpUrl = request.url

        logInfo.apply {
            host = reqHttpUrl.host
            path = reqHttpUrl.encodedPath
            requestParamsMapString = getUrlRequestParams(request)
            requestBody = postRequestParams(request)
            requestHeaders = getHeadersString(request.headers)
            tookTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime)
            requestType = request.method
            isSuccessRequest = false
            responseCode = response.code.toString()
            responseHeaders = getHeadersString(response.headers)
            time = System.currentTimeMillis()
        }
        return logInfo
    }

    private fun parseSuccessHttpLog(
        response: Response,
        request: Request,
        startTime: Long
    ): GiraffeHttpLogInfo {
        val logInfo = GiraffeHttpLogInfo()
        if (response.promisesBody()) {
            val responseBody = response.body
            val contentLength = responseBody!!.contentLength()
            val bodySize =
                if (contentLength != -1L) (contentLength).toString() + "-byte" else "unknown-length"

            val reqHttpUrl = request.url

            if (supportParseType(responseBody.contentType())

                && !bodyHasUnknownEncoding(response.headers)
            ) {
                val source = responseBody.source()
                source.request(java.lang.Long.MAX_VALUE)    // Buffer the entire body.
                val buffer = source.buffer()

                var resStr = ""
                if (contentLength != 0L) {
                    resStr = buffer.clone().readString(Charset.forName("UTF-8"))
                }

                logInfo.apply {
                    host = reqHttpUrl.host
                    path = reqHttpUrl.encodedPath
                    requestParamsMapString = getUrlRequestParams(
                        request
                    )
                    requestBody = postRequestParams(
                        request
                    )
                    requestHeaders = getHeadersString(request.headers)
                    responseStr = resStr
                    responseHeaders = getHeadersString(response.headers)
                    tookTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime)
                    size = bodySize
                    requestType = request.method
                    isSuccessRequest = true
                    time = System.currentTimeMillis()
                    responseContentType = responseBody.contentType()?.type ?: ""
                }
            }
        }

        try {
            logInfo.curlBean = CUrlHelper.convertOKRequestToCUrlInfo(request)
            GiraffeLog.d("curltest", "str: ${logInfo.curlBean}")
        } catch (e: Exception) {
            GiraffeLog.d("curltest", "convertOKRequestToCUrlInfo error: $e")
        }

        return logInfo
    }

    private fun postRequestParams(request: Request): String {
        if (request.method != "POST" && request.body != null && request.body!!.contentLength() > 0) return ""
        if (request.body is MultipartBody) {
            return "【文件上传】"
        }
        val requestBuffer = Buffer()
        request.body?.writeTo(requestBuffer)
        return String(requestBuffer.readByteArray())
    }

    private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"]
        return (contentEncoding != null
                && !contentEncoding.equals("identity", ignoreCase = true)
                && !contentEncoding.equals("gzip", ignoreCase = true))
    }


    /**
     * 获取 GET 请求的请求参数
     * */
    private fun getUrlRequestParams(request: Request): String {
        val requestUrl = request.url.toString()
        val requestMap = HashMap<String, String>()
        try {
            val charset = "utf-8"
            val decodedUrl = URLDecoder.decode(requestUrl, charset)
            if (decodedUrl.indexOf('?') != -1) {
                val contents = decodedUrl.substring(decodedUrl.indexOf('?') + 1)
                val keyValues =
                    contents.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (i in keyValues.indices) {
                    val key = keyValues[i].substring(0, keyValues[i].indexOf("="))
                    val value = keyValues[i].substring(keyValues[i].indexOf("=") + 1)
                    requestMap[key] = value
                }
            }

            return CommJsonParser.getCommJsonParser().toJson(requestMap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""

    }

    private fun getHeadersString(
        headers: Headers
    ): String {
        return CommJsonParser.getCommJsonParser().toJson(
            headers.toMultimap()
        )
    }

    private fun supportParseType(contentType: MediaType?): Boolean {
        return SUPPORT_PARSE_TYPE.contains(contentType?.subtype)
    }

    private val SUPPORT_PARSE_TYPE = arrayOf("json", "GSON")

    private fun isSuccessResponse(code: Int) = code == 200

    private fun isErrorResponse(code: Int) = code in 400..599

    fun createExceptionLog(request: Request, e: Exception): GiraffeHttpLogInfo {
        val logInfo = GiraffeHttpLogInfo()
        val reqHttpUrl = request.url
        logInfo.apply {
            host = reqHttpUrl.host
            path = reqHttpUrl.encodedPath
            requestParamsMapString = getUrlRequestParams(request)
            requestBody = ""
            requestHeaders = getHeadersString(request.headers)
            responseStr = e.message
            responseHeaders = ""
            tookTime = 0
            requestType = request.method
            isExceptionRequest = true
            responseCode = e.message
            time = System.currentTimeMillis()
        }
        return logInfo
    }
}
