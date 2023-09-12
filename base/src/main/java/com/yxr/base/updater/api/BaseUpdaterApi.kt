package com.yxr.base.updater.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface BaseUpdaterApi {
    @GET
    @Streaming
    fun downloadApp(@Url appUrl: String): Call<ResponseBody>
}