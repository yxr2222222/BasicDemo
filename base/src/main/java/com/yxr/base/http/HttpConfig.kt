package com.yxr.base.http

import android.annotation.SuppressLint
import com.yxr.base.http.callback.IHttpConfigCallback
import okhttp3.Cache
import okhttp3.Interceptor
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*

/**
 * @author ciba
 * @description 网络请求配置信息
 * @date 2020/9/17
 */
class HttpConfig private constructor(
    var sslSocketFactory: SSLSocketFactory?,
    var x509TrustManager: X509TrustManager,
    var hostnameVerifier: HostnameVerifier
) {
    /**
     * 超时时长，不是最终的，是每个环节的超时时长
     */
    var timeout: Long = 15
        private set

    /**
     * 失败重试次数
     */
    var retryNum: Int = 0
        private set

    /**
     * 缓存
     */
    var cache: Cache? = null
        private set

    /**
     * 基础地址
     */
    var baseUrl: String = ""
        private set

    /**
     * 是否是调试
     */
    var isDebug = false
        private set

    /**
     * config callback
     */
    var configCallback: IHttpConfigCallback? = null
        private set

    /**
     * baseUrlReplaceConfig
     */
    var baseUrlReplaceConfig: BaseUrlReplaceConfig? = null
        private set

    /**
     * 拦截器列表
     */
    val interceptorList = mutableListOf<Interceptor>()

    val globalErrorCodeList = mutableListOf<Int>()

    class Builder {
        private val httpConfig =
            HttpConfig(createSSLSocketFactory(), TrustAllCerts(), TrustAllHostnameVerifier())

        /**
         * 是否是调试模式
         */
        fun isDebug(isDebug: Boolean): Builder {
            httpConfig.isDebug = isDebug
            return this
        }

        /**
         * 设置超时时长，单位秒
         *
         * @param timeout 超时时长
         */
        fun timeout(timeout: Long): Builder {
            httpConfig.timeout = timeout
            return this
        }

        /**
         * 设置失败重试次数
         *
         * @param retryNum 失败重试次数
         */
        fun retryNum(retryNum: Int): Builder {
            httpConfig.retryNum = retryNum
            return this
        }

        fun addGlobalErrorCode(errorCode: Int): Builder {
            if (!httpConfig.globalErrorCodeList.contains(errorCode)) {
                httpConfig.globalErrorCodeList.add(errorCode)
            }
            return this
        }

        /**
         * 设置缓存
         *
         * @param cache 缓存
         */
        fun cache(cache: Cache): Builder {
            httpConfig.cache = cache
            return this
        }

        /**
         * 设置网络请求BaseUrl
         */
        fun baseUrl(baseUrl: String): Builder {
            httpConfig.baseUrl = baseUrl
            return this
        }

        /**
         * 设置网络请求拦截器
         *
         * @param interceptor 拦截器
         */
        fun addInterceptor(interceptor: Interceptor): Builder {
            httpConfig.interceptorList.add(interceptor)
            return this
        }

        /**
         * Config Callback
         */
        fun callback(configCallback: IHttpConfigCallback): Builder {
            httpConfig.configCallback = configCallback
            return this
        }

        /**
         * baseUrlReplaceConfig
         */
        fun baseUrlReplaceConfig(baseUrlReplaceConfig: BaseUrlReplaceConfig): Builder {
            httpConfig.baseUrlReplaceConfig = baseUrlReplaceConfig
            return this
        }

        fun sslSocketFactory(sslSocketFactory: SSLSocketFactory?): Builder {
            httpConfig.sslSocketFactory = sslSocketFactory
            return this
        }

        fun x509TrustManager(x509TrustManager: X509TrustManager): Builder {
            httpConfig.x509TrustManager = x509TrustManager
            return this
        }

        fun hostnameVerifier(hostnameVerifier: HostnameVerifier): Builder {
            httpConfig.hostnameVerifier = hostnameVerifier
            return this
        }

        fun build(): HttpConfig {
            return httpConfig
        }

        @SuppressLint("CustomX509TrustManager")
        private class TrustAllCerts : X509TrustManager {
            @SuppressLint("TrustAllX509TrustManager")
            @Throws(CertificateException::class)
            override fun checkClientTrusted(x509Certificates: Array<X509Certificate>, s: String) {
            }

            @SuppressLint("TrustAllX509TrustManager")
            @Throws(CertificateException::class)
            override fun checkServerTrusted(x509Certificates: Array<X509Certificate>, s: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }

        private class TrustAllHostnameVerifier : HostnameVerifier {
            @SuppressLint("BadHostnameVerifier")
            override fun verify(hostname: String, session: SSLSession): Boolean {
                return true
            }
        }

        companion object {
            private fun createSSLSocketFactory(): SSLSocketFactory? {
                var ssfFactory: SSLSocketFactory? = null
                try {
                    val sc = SSLContext.getInstance("TLS")
                    sc.init(null, arrayOf<TrustManager>(TrustAllCerts()), SecureRandom())
                    ssfFactory = sc.socketFactory
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return ssfFactory
            }
        }
    }
}