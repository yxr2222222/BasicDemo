package com.yxr.base.util;

import android.annotation.SuppressLint;
import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@SuppressLint("SimpleDateFormat")
public class DateUtil {
    private static final String DEFAULT_PATTERN = "yyyy/MM/dd HH:mm:ss";
    private static final float MIN = 60f * 1000;
    private static final float HOUR = MIN * 60;
    private static final float DAY = 24 * HOUR;

    public static String timeMillisFormat(long timeMillis) {
        return timeMillisFormat(timeMillis, DEFAULT_PATTERN);
    }

    /**
     * 将毫秒级别的时长转换成时分秒
     *
     * @param durationMillis 毫秒级别时长
     */
    public static String durationFormat(long durationMillis) {
        return timeMillisFormat(durationMillis, "HH:mm:ss");
    }

    /**
     * 毫秒时间戳格式化
     *
     * @param timeMillis 毫秒级别时间戳
     * @param pattern    格式化格式
     */
    public static String timeMillisFormat(long timeMillis, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        format.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        return format.format(new Date(timeMillis));
    }

    /**
     * 获取相对时间跨度字符串，例如 刚刚，1分钟前，1小时前...
     *
     * @param timeMillis 毫秒级时间戳
     */
    public static String getRelativeTimeSpanString(long timeMillis) {
        try {
            return DateUtils.getRelativeTimeSpanString(timeMillis).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取时长格式化数据，
     *
     * @param durationMillis 毫秒级别时长
     */
    @SuppressLint("DefaultLocale")
    private String getDurationFormat(int durationMillis) {
        if (durationMillis > DAY) {
            return String.format("%.1f%s", durationMillis / DAY, "d");
        } else if (durationMillis > HOUR) {
            return String.format("%.1f%s", durationMillis / HOUR, "h");
        }
        return String.format("%.1f%s", durationMillis / MIN, "m");
    }
}
