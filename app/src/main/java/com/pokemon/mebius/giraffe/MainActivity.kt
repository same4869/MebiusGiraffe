package com.pokemon.mebius.giraffe

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.gson.Gson
import com.pokemon.mebius.commlib.utils.APPLICATION
import com.pokemon.mebius.commlib.utils.CommJsonParser
import com.pokemon.mebius.commlib.utils.JsonParser
import com.pokemon.mebius.commlib.utils.showToast
import com.pokemon.mebius.giraffe.base.GiraffeMonitorProtocol
import com.pokemon.mebius.giraffe.config.GiraffeConfig
import com.pokemon.mebius.giraffe.demo.R
import com.pokemon.mebius.giraffe.monitor.GiraffeMonitor
import com.pokemon.mebius.log.MLog
import com.pokemon.mebius.restful.RetrofitClient
import com.pokemon.mebius.restful.exception.ApiException
import com.pokemon.mebius.restful.exception.BaseErrorConsumer
import com.pokemon.mebius.restful.utils.applySchedulers
import java.lang.reflect.Type

class MainActivity : AppCompatActivity() {
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        APPLICATION = application

        val giraffeConfig = GiraffeConfig()
        giraffeConfig.monitorConfig.autoOpenMonitors.addAll(
            hashSetOf(
                GiraffeMonitorProtocol.EXCEPTION.name,
                GiraffeMonitorProtocol.NET.name
            )
        )
        Giraffe.init(application, giraffeConfig)

        Giraffe.open(true, this)

        findViewById<Button>(R.id.test_crash).setOnClickListener {
            var a: String? = null
            a!!.length
        }

        initJsonParser()
        RetrofitClient.init(baseUrlP = "https://www.wanandroid.com/", interceptorBuilder = {
            it.addInterceptor(GiraffeMonitor.getNetMonitor())
        })
        findViewById<Button>(R.id.test_net).setOnClickListener {
            RetrofitClient.getOrCreateService<ApiService>().requestHotKey().applySchedulers()
                .subscribe({
                    MLog.d("restful requestHotKey --> ${it.data}")
                    showToast("requestHotKey --> ${it.data}")
                }, object : BaseErrorConsumer(mBlock = { errCode, errMsg ->
                    MLog.d("restful mBlock errCode --> $errCode errMsg --> $errMsg")
                }) {
                    override fun onApiExceptionCall(apiException: ApiException) {
                        MLog.d("restful onApiExceptionCall errCode --> ${apiException.errorCode} errMsg --> ${apiException.errorMessage}")
                    }

                    override fun logError(e: Throwable) {
                        MLog.d("restful logError  --> ${e.message}")
                    }
                })
        }
    }

    private fun initJsonParser() {
        CommJsonParser.initJsonParser(jsonParserP = object : JsonParser {
            override fun toJson(src: Any): String {
                return Gson().toJson(src)
            }

            override fun <T> fromJson(json: String, clazz: Class<T>): T {
                return Gson().fromJson(json, clazz)
            }

            override fun <T> fromJson(json: String, typeOfT: Type): T {
                return Gson().fromJson(json, typeOfT)
            }

        })
    }
}