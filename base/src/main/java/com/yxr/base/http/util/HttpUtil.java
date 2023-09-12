package com.yxr.base.http.util;

public class HttpUtil {
    private static final String HTTP = "http://";
    private static final String HTTPS = "https://";

    public static boolean isUrl(String url) {
        return url != null && (url.startsWith(HTTP) || url.startsWith(HTTPS));
    }
}