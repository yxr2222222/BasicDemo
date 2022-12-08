package com.yxr.basicdemo

import com.yxr.base.BaseApplication
import com.yxr.base.http.HttpConfig
import com.yxr.base.http.callback.IHttpConfigCallback
import com.yxr.base.util.PathUtil
import okhttp3.Cache

class MyApp : BaseApplication() {
    companion object {
        val headers = mutableMapOf("os" to "Android", "version" to "1.0")
    }

    override fun getHttpConfig(): HttpConfig {
        return HttpConfig.Builder()
            // 是否是Debug，Debug模式会添加HttpLoggingInterceptor，默认false
            .isDebug(BuildConfig.DEBUG)
            // 设置BaseUrl，必须设置
            .baseUrl("http://www.baidu.com")
            // 设置每个步骤的超时时间，单位秒，默认10s
            .timeout(10)
            // 设置网络请求失败重试次数，默认3
            .retryNum(3)
            // 设置缓存，不需要可不设置
            .cache(Cache(PathUtil.getDir("/http-cache"), maxSize = 1024 * 1024 * 1024))
            // 添加拦截器，不需要可不设置
            .addInterceptor { chain -> chain.proceed(chain.request()) }
            // 设置网络配置回调，不需要可不设置
            .callback(object : IHttpConfigCallback {
                override fun getPublicHeaders(): MutableMap<String, String> {
                    return headers
                }

            })
            .build()
    }
}