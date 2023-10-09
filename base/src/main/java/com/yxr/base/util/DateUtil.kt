package com.yxr.base.util

import android.annotation.SuppressLint
import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

class DateUtil {
    companion object {
        private const val DEFAULT_PATTERN = "yyyy/MM/dd HH:mm:ss"
        private const val MIN = 60f * 1000
        private const val HOUR = MIN * 60
        private const val DAY = 24 * HOUR

        @JvmStatic
        fun timeMillisFormat(timeMillis: Long): String? {
            return timeMillisFormat(timeMillis, DEFAULT_PATTERN)
        }

        /**
         * 将毫秒级别的时长转换成时分秒
         *
         * @param durationMillis 毫秒级别时长
         */
        @JvmStatic
        fun durationFormat(durationMillis: Long): String? {
            return timeMillisFormat(durationMillis, "HH:mm:ss")
        }

        /**
         * 毫秒时间戳格式化
         *
         * @param timeMillis 毫秒级别时间戳
         * @param pattern    格式化格式
         */
        @SuppressLint("SimpleDateFormat")
        @JvmStatic
        fun timeMillisFormat(timeMillis: Long, pattern: String?): String? {
            val format = SimpleDateFormat(pattern)
            format.timeZone = TimeZone.getTimeZone("GMT+00:00")
            return format.format(Date(timeMillis))
        }

        /**
         * 获取相对时间跨度字符串，例如 刚刚，1分钟前，1小时前...
         *
         * @param timeMillis 毫秒级时间戳
         */
        @JvmStatic
        fun getRelativeTimeSpanString(timeMillis: Long): String {
            try {
                return DateUtils.getRelativeTimeSpanString(timeMillis).toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }

        /**
         * 获取时长格式化数据，
         *
         * @param durationMillis 毫秒级别时长
         */
        @SuppressLint("DefaultLocale")
        @JvmStatic
        private fun getDurationFormat(durationMillis: Int): String? {
            if (durationMillis > DAY) {
                return String.format("%.1f%s", durationMillis / DAY, "d")
            } else if (durationMillis > HOUR) {
                return String.format("%.1f%s", durationMillis / HOUR, "h")
            }
            return String.format("%.1f%s", durationMillis / MIN, "m")
        }
    }
}