package com.yxr.base.http.cache

import androidx.annotation.StringDef

object CacheMode {

    /**
     * 只请求网络，默认 (不加缓存)
     */
    const val ONLY_NETWORK = "ONLY_NETWORK"

    /**
     * 只读取缓存(没有缓存抛出异常)
     */
    const val ONLY_CACHE = "ONLY_CACHE"

    /**
     * 先读取缓存，缓存失效再请求网络更新缓存
     */
    const val READ_CACHE_NETWORK_PUT = "READ_CACHE_NETWORK_PUT"

    /**
     * 先请求网络，网络请求失败使用缓存  (网络请求成功，写入缓存)
     */
    const val NETWORK_PUT_READ_CACHE = "NETWORK_PUT_READ_CACHE"

    @StringDef(
        ONLY_NETWORK,
        ONLY_CACHE,
        READ_CACHE_NETWORK_PUT,
        NETWORK_PUT_READ_CACHE
    )
    annotation class CacheModel

}