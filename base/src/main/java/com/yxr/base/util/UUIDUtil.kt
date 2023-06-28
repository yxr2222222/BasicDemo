package com.yxr.base.util

import java.util.*

class UUIDUtil {
    companion object {
        /**
         * 获取uuid
         *
         * @param length : 需要保留的长度
         */
        @JvmStatic
        fun uuid(length: Int = -1): String {
            var tempLength = length
            val uuid = UUID.randomUUID().toString().replace("-", "")
            if (length < 0 || length > uuid.length) {
                tempLength = uuid.length
            }
            return uuid.substring(0, tempLength)
        }
    }
}