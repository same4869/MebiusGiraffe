package com.pokemon.mebius.giraffe.base.entities

import android.text.TextUtils
import com.google.gson.Gson
import org.greenrobot.greendao.converter.PropertyConverter

/**
 * 用来存储要转为curl格式的原始request网络请求数据
 */
class CurlRequestInfo {
    var headers: List<Header> = arrayListOf()
    var url: String = ""
    var requestMethod: String = ""
    var body: Body = Body()

    class Header(val key: String, val value: String) {

    }

    class Body {
        var data: String = ""
        var contentType: String = ""
    }

    companion object {
        class CurlRequestConverter :
            PropertyConverter<CurlRequestInfo?, String?> {
            //将数据库中的类型转换成java类型
            override fun convertToEntityProperty(databaseValue: String?): CurlRequestInfo? {
                return if (TextUtils.isEmpty(databaseValue)) {
                    null
                } else GSON.fromJson(
                    databaseValue,
                    CurlRequestInfo::class.java
                )
            }

            //将java类型转换成数据库类型
            override fun convertToDatabaseValue(entityProperty: CurlRequestInfo?): String? {
                return if (entityProperty == null) {
                    null
                } else GSON.toJson(entityProperty)
            }

            companion object {
                private val GSON = Gson()
            }
        }
    }

}