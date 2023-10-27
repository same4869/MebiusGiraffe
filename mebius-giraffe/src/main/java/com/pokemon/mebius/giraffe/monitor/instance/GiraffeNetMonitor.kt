package com.pokemon.mebius.giraffe.monitor.instance

import android.content.Context
import com.pokemon.mebius.giraffe.base.GiraffeLog
import com.pokemon.mebius.giraffe.base.GiraffeMonitorProtocol
import com.pokemon.mebius.giraffe.base.TAG_MONITOR
import com.pokemon.mebius.giraffe.monitor.utils.GiraffeHttpResponseParser
import com.pokemon.mebius.giraffe.storage.GiraffeStorage
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/6/9
 */

internal class GiraffeNetMonitor(override var isOpen: Boolean = false) : GiraffeMonitorProtocol,
    Interceptor {

    private var startNs = System.nanoTime()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        startNs = System.nanoTime()

        try {
            val response = chain.proceed(request)

            if (!isOpen) return response

            monitorHttpLog(request, response)

            monitorHttpCostTime(request, response)

            return response

        } catch (e: Exception) {

            if (isOpen) {
                GiraffeStorage.save(GiraffeHttpResponseParser.createExceptionLog(request, e))
            }

            throw e
        }
    }

    private fun monitorHttpLog(request: Request, response: Response) {
        try {
            GiraffeLog.d("startNs:$startNs")
            val logInfo = GiraffeHttpResponseParser.parserResponse(request, response, startNs)

            if (logInfo.isvalid()) {
                GiraffeLog.d("logInfo time:${logInfo.time} path:${logInfo.path}")
                GiraffeStorage.save(logInfo)
            }

        } catch (e: Exception) {
            GiraffeLog.d(TAG_MONITOR, "GiraffeHttpLogInterceptor error : ${e.printStackTrace()}")
        }
    }

    private fun monitorHttpCostTime(request: Request, response: Response) {

//        val requestUrl = request.url.toString()
//
////        if (!GiraffeMonitor.monitorRequest(requestUrl)) return
//
//        try {
//
//            val costTime = System.nanoTime() - startNs

//            GiraffeMonitor.markRequestFinish(
//                requestUrl,
//                TimeUnit.MILLISECONDS.convert(costTime, TimeUnit.NANOSECONDS)
//            )

//        } catch (e: Exception) {
//            GiraffeLog.d("GiraffeHttpLogInterceptor error : ${e.printStackTrace()}")
//        }
    }


    override fun open(context: Context) {
        isOpen = true
    }

    override fun close() {
        isOpen = false
    }

    override fun getMonitorInfo() = GiraffeMonitorProtocol.NET


}