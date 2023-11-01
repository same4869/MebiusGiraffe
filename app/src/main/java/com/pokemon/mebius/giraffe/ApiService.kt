package com.pokemon.mebius.giraffe

import io.reactivex.Observable
import retrofit2.http.GET


interface ApiService {
    @GET("hotkey/json")
    fun requestHotKey(): Observable<CommonResponseInfo<List<HotKeyBean>>>
}

data class CommonResponseInfo<T>(
    val data: T
)

data class HotKeyBean(
    val id: Int,
    val link: String,
    val name: String,
    val order: Int,
    val visible: Int
)