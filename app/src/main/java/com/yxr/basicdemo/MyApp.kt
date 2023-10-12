package com.yxr.basicdemo

import android.util.Log
import com.yxr.base.BaseApplication
import com.yxr.base.http.BaseUrlReplaceConfig
import com.yxr.base.http.HttpConfig
import com.yxr.base.http.cache.CacheConfig
import com.yxr.base.http.callback.IHttpConfigCallback
import com.yxr.base.http.callback.OnBaseUrlReplaceCallback
import com.yxr.base.http.manager.HttpManager
import com.yxr.base.util.PathUtil
import com.yxr.basicdemo.model.BaseResponse
import okhttp3.Cache
import okhttp3.HttpUrl

class MyApp : BaseApplication() {
    companion object {
        val headers = mutableMapOf("os" to "Android", "version" to "1.0")
    }

    override fun getHttpConfig(): HttpConfig {
        return HttpConfig.Builder()
            // 是否是Debug，Debug模式会添加HttpLoggingInterceptor，默认false
            .isDebug(BuildConfig.DEBUG)
            // 设置BaseUrl，必须设置
            .baseUrl("http://192.168.2.42:7722")
            // 设置每个步骤的超时时间，单位秒，默认10s
            .timeout(10)
            // 设置网络请求失败重试次数，默认0
            .retryNum(0)
            // 设置缓存，不需要可不设置
            .cache(
                CacheConfig(
                    cls = BaseResponse::class.java,
                    directory = PathUtil.getDir("/http-cache"),
                    maxSize = 1024 * 1024 * 1024
                )
            )
            // 添加拦截器，不需要可不设置
            .addInterceptor { chain -> chain.proceed(chain.request()) }
            // 设置网络环境切换配置，不需要可不设置
            .baseUrlReplaceConfig(
                BaseUrlReplaceConfig.Builder()
                    // 测试环境地址
                    .debugBaseUrl("http://192.168.2.42:7722")
                    // 正式环境地址
                    .formalBaseUrl("http://192.168.2.42:7722")
                    // 其他地址，没啥作用，用来切换界面快速切换的，可设置多个
                    .otherBaseUrl("https://www.baidu.com")
                    // 其他地址，没啥作用，用来切换界面快速切换的，可设置多个
                    .otherBaseUrl("https://github.com/")
                    .onBaseUrlReplaceCallback(object : OnBaseUrlReplaceCallback {
                        override fun getCustomFormalBaseUrl(): String? {
                            // 获取自定义的正式环境的BaseUrl，返回空的或者不是网络地址将使用@{@link com.yxr.base.http.BaseUrlReplaceConfig}的formalBaseUrl
                            return null
                        }

                        override fun onBaseUrlReplace(oldHost: String?, newHost: String?) {
                            // BaseUrl被替换了
                            Log.d(
                                HttpManager.TAG,
                                "onBaseUrlReplace oldHost: $oldHost, newHost: $newHost"
                            )
                        }
                    })
                    .build()
            )
            // 设置网络配置回调，不需要可不设置
            .callback(object : IHttpConfigCallback {
                override fun getPublicHeaders(httpUrl: HttpUrl): MutableMap<String, String> {
                    return headers
                }

                override fun onGlobalError(code: Int) {

                }
            })
            .build()
    }
}