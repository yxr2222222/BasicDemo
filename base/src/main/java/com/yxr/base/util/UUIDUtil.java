package com.yxr.base.util;

import java.util.UUID;

/**
 * @author ciba
 * @description UUID工具类
 * @date 2020/09/17
 */
public class UUIDUtil {
    /**
     * 获取uuid
     *
     * @param length : 需要保留的长度
     */
    public static String uuid(int length) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        if (length < 0) {
            length = 0;
        } else if (length > uuid.length()) {
            length = uuid.length();
        }
        return uuid.substring(0, length);
    }
}
