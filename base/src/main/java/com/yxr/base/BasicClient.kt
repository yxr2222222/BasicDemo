package com.yxr.base

import android.annotation.SuppressLint
import android.content.Context
import com.tencent.mmkv.MMKV
import com.yxr.base.http.HttpConfig
import com.yxr.base.http.manager.HttpManager

class BasicClient {
    companion object {
        @SuppressLint("StaticFieldLeak")
        @JvmField
        val instance = BasicClient()
    }

    lateinit var context: Context

    fun init(context: Context, httpConfig: HttpConfig) {
        if (!this::context.isLateinit) {
            this.context = context.applicationContext
            // 初始化MMKV
            MMKV.initialize(context.applicationContext)
            // 初始化网络请求
            HttpManager.get().init(httpConfig)
        }
    }
}