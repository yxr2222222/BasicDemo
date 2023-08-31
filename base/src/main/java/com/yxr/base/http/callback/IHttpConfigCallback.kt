package com.yxr.base.http.callback

import okhttp3.HttpUrl

interface IHttpConfigCallback {
    fun getPublicHeaders(httpUrl: HttpUrl): MutableMap<String, String>

    fun onGlobalError(code: Int)
}