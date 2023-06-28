package com.yxr.base.http.interceptor

import com.yxr.base.BaseApplication
import com.yxr.base.http.extension.isNetworkConnected
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

class CacheInterceptor : Interceptor {
    companion object {
        const val CACHE_HEADER = "Cache-Time"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val cacheTimeHeader = request.header(CACHE_HEADER)
        if (cacheTimeHeader.isNullOrBlank()) {
            request.newBuilder().cacheControl(
                CacheControl.Builder().noCache().noStore().build()
            )
            return chain.proceed(request)
        } else {
            val isNetworkConnected = BaseApplication.context.isNetworkConnected()

            if (!isNetworkConnected) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build()
            }

            return chain.proceed(request).newBuilder()
                .removeHeader("Pragma")
                // cache for cache seconds
                .header("Cache-Control", "max-age=$cacheTimeHeader")
                .build()
        }
    }
}