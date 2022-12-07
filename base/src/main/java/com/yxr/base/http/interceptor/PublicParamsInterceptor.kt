package com.yxr.base.http.interceptor

import com.yxr.base.http.manager.HttpManager
import okhttp3.Interceptor
import okhttp3.Response

class PublicParamsInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        // 添加公共头
        HttpManager.get().httpConfig.configCallback?.getPublicHeaders()?.forEach { header ->
            builder.addHeader(header.key, header.value)
        }
        return chain.proceed(builder.build())
    }
}