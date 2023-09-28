package com.yxr.basicdemo.config

import com.yxr.base.util.MMKVUtil
import com.yxr.basicdemo.model.CurrBook

class MMKVConfig {
    companion object {
        // 是否需要展示隐私政策
        var isNeedShowPrivacy = MMKVUtil.getBoolean("isNeedShowPrivacy", true)
            set(value) {
                field = value
                MMKVUtil.putBoolean("isNeedShowPrivacy", value)
            }

        // 当前正在阅读的书
        var currBook = MMKVUtil.getData("currBook", CurrBook::class.java, null)
            set(value) {
                field = value
                MMKVUtil.putData("currBook", value)
            }
    }
}