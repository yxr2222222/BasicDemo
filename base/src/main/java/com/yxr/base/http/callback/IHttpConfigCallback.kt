package com.yxr.base.http.callback

interface IHttpConfigCallback {
    fun getPublicHeaders(): MutableMap<String, String>
}