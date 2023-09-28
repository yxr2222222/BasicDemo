package com.yxr.base.http.cache

import java.io.File

data class CacheConfig(
    val directory: File,
    val maxSize: Long = 500 * 1024 * 1024,
    val appVersion: Int = 1,
    val valueCount: Int = 2,
)