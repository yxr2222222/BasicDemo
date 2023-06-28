package com.yxr.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.tencent.mmkv.MMKV
import com.yxr.base.http.HttpConfig
import com.yxr.base.http.manager.HttpManager

abstract class BaseApplication : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        @JvmStatic
        lateinit var context: Context
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        MMKV.initialize(this)
        HttpManager.get().init(getHttpConfig())
    }

    /**
     * 获取网络配置信息
     *
     * @return 网络配置信息
     */
    protected abstract fun getHttpConfig(): HttpConfig
}