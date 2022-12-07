package com.yxr.base.http.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class RetryInterceptor(private val retryNum: Int = 3) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)
        var currNum = 1
        while (!response.isSuccessful && currNum < retryNum) {
            currNum++
            response = chain.proceed(request)
        }
        return response
    }
}