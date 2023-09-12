package com.yxr.base.http.manager

import android.content.Context
import android.util.Log
import com.yxr.base.activity.BaseUrlReplaceEditActivity
import com.yxr.base.extension.startSimpleActivity
import com.yxr.base.http.HttpConfig
import com.yxr.base.http.interceptor.BaseUrlReplaceInterceptor
import com.yxr.base.http.interceptor.CacheInterceptor
import com.yxr.base.http.interceptor.PublicParamsInterceptor
import com.yxr.base.http.interceptor.RetryInterceptor
import com.yxr.base.util.ToastUtil
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author ciba
 * @description 接口管理类
 * @date 2020/09/17
 */
class HttpManager private constructor() {

    lateinit var httpConfig: HttpConfig
        private set

    companion object {
        const val TAG = "HttpManager"
        private const val TIME_OUT: Long = 10
        private var instance: HttpManager? = null

        @JvmStatic
        fun get(): HttpManager {
            if (instance == null) {
                synchronized(HttpManager::class.java) {
                    if (instance == null) {
                        instance = HttpManager()
                    }
                }
            }
            return instance!!
        }
    }

    private val jsonRetrofitMap = HashMap<Int?, Retrofit>()
    private val xmlRetrofitMap = HashMap<Int?, Retrofit>()
    private val apiClassMap = HashMap<String, Any>()

    /**
     * 初始化OkHttpClient,retrofit
     *
     * @param httpConfig 网络请求配置参数
     */
    fun init(httpConfig: HttpConfig) {
        this.httpConfig = httpConfig
    }

    fun <T : Any> createApi(
        cls: Class<T>,
        dispatcher: Dispatcher? = null,
        isJson: Boolean = true
    ): T {
        val apiKey = cls.name + dispatcher.hashCode()
        var api = apiClassMap[apiKey]
        if (api == null) {
            api = getRetrofit(dispatcher, isJson).create(cls)
            apiClassMap[apiKey] = api
        }
        return api as T
    }

    fun getRetrofit(dispatcher: Dispatcher? = null, isJson: Boolean = true): Retrofit {
        return if (isJson) {
            jsonRetrofitMap[dispatcher?.hashCode()] ?: createRetrofit(dispatcher, true)
        } else {
            xmlRetrofitMap[dispatcher?.hashCode()] ?: createRetrofit(dispatcher, false)
        }
    }

    /**
     * 创建Retrofit
     */
    private fun createRetrofit(dispatcher: Dispatcher?, isJson: Boolean): Retrofit {
        val builder = OkHttpClient().newBuilder().cache(httpConfig.cache)

        // 设置调度器
        if (dispatcher != null) {
            builder.dispatcher(dispatcher)
        }

        // 添加BaseUrl替换拦截器
        val baseUrlReplaceConfig = httpConfig.baseUrlReplaceConfig
        if (baseUrlReplaceConfig != null) {
            builder.addInterceptor(BaseUrlReplaceInterceptor(baseUrlReplaceConfig))
        }
        // 添加缓存拦截器
        builder.addInterceptor(CacheInterceptor())
        builder.addNetworkInterceptor(CacheInterceptor())
        // 添加公共参数拦截器
        builder.addInterceptor(PublicParamsInterceptor())
        // 添加重试拦截器
        builder.addInterceptor(RetryInterceptor(httpConfig.retryNum))

        // 添加用户自定义拦截器
        httpConfig.interceptorList.forEach { interceptor ->
            builder.addInterceptor(interceptor)
        }

        if (httpConfig.isDebug) {
            // 添加日志拦截器
            Log.d(TAG, "addHttpLoggingInterceptor")
            val httpLoggingInterceptor = HttpLoggingInterceptor {
                Log.d(TAG, "data : $it")
            }
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(httpLoggingInterceptor)
        }

        val timeout = if (httpConfig.timeout > 0) httpConfig.timeout else TIME_OUT
        builder.connectTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .hostnameVerifier(httpConfig.hostnameVerifier)

        httpConfig.sslSocketFactory?.let { sslSocketFactory ->
            builder.sslSocketFactory(sslSocketFactory, httpConfig.x509TrustManager)
        }

        val client: OkHttpClient = builder.build()
        if (isJson) {
            val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(httpConfig.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
            jsonRetrofitMap[dispatcher?.hashCode()] = retrofit
            return retrofit
        } else {
            val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(httpConfig.baseUrl)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
            xmlRetrofitMap[dispatcher?.hashCode()] = retrofit
            return retrofit
        }
    }

    fun startBaseUrlEditActivity(context: Context) {
        httpConfig.baseUrlReplaceConfig?.let {
            context.startSimpleActivity(BaseUrlReplaceEditActivity::class.java)
        } ?: run {
            ToastUtil.show("请先初始化BaseUrlReplaceConfig")
        }
    }

    fun updateDebugUrl(baseUrl: String) {
        httpConfig.baseUrlReplaceConfig?.updateDebugUrl(baseUrl)
    }
}