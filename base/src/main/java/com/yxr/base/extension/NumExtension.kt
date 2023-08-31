package com.yxr.base.extension

import com.yxr.base.model.HourMinute

/**
 * 小于0的正整数在前面补0
 */
const val MINUTE = 60 * 1000
const val HOUR = 60 * MINUTE

fun Int.fixToString(): String = if (this > 9) this.toString() else "0$this"

fun String.toDoubleWithDefault(defaultValue: Double = 0.0): Double = try {
    this.toDouble()
} catch (e: Throwable) {
    e.printStackTrace()
    defaultValue
}

fun Long.toHoursMinutes(): HourMinute {
    if (this >= HOUR) {
        val hour = this / HOUR
        val minute = (this - hour * HOUR) / MINUTE
        return HourMinute(hour.toInt(), minute.toInt())
    }
    return HourMinute(0, (this / MINUTE).toInt())
}
