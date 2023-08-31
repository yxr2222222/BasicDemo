package com.yxr.base.extension

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 * 计算两个日期相差多少天
 */
fun Date.diffDay(targetDate: Date): Int {
    val calendar = Calendar.getInstance()

    calendar.time = this
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val startTime = calendar.time.time

    calendar.time = targetDate
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val targetTime = calendar.time.time

    return ((startTime - targetTime) / (24 * 60 * 60 * 1000)).toInt()
}

/**
 * 字符串转时间Date
 */
@SuppressLint("SimpleDateFormat")
fun String.formatDate(format: String = "yyyy-MM-dd HH:mm:ss") = try {
    SimpleDateFormat(format).parse(this)
} catch (e: Throwable) {
    e.printStackTrace()
    null
}

@SuppressLint("SimpleDateFormat")
fun Date.formatDateToString(format: String = "yyyy-MM-dd HH:mm:ss") = try {
    SimpleDateFormat(format).format(this)
} catch (e: Throwable) {
    e.printStackTrace()
    null
}