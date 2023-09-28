package com.yxr.base.http.cache

data class CacheStrategy(
    var cacheKey: String = "", // 缓存key
    val cacheTime: Long = -1, // 过期时间, 默认-1,不过期, 单位秒
    @CacheMode.CacheModel val cacheMode: String? = null // 缓存模式
) {
    companion object {

        const val CACHE_MODE = "Custom-Cache-Mode"

        const val CACHE_TIME = "Custom-Cache-Time"

        const val CUSTOM_CACHE_KEY = "Custom-Cache-Key"

        const val MINUTE = 60
        const val HOUR = 60 * MINUTE
        const val DAY = 24 * HOUR
        const val WEEK = 7 * DAY
        const val MONTH = 30 * DAY
    }
}