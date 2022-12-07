package com.yxr.basicdemo

import com.yxr.base.BaseApplication
import com.yxr.base.http.HttpConfig

class App : BaseApplication() {
    override fun getHttpConfig(): HttpConfig {
        return HttpConfig.Builder()
            .baseUrl("http://www.baidu.com")
            .build()
    }
}