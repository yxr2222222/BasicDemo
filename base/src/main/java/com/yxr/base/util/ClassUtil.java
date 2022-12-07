package com.yxr.base.util;

/**
 * @author ciba
 * @date 2020/11/18
 * @description 类工具
 */

public class ClassUtil {
    /**
     * 根据完整路径类名反射获取对象
     *
     * @param className ：完整路径类名
     * @param <T>       ：对象类型
     * @return ：反射对象
     */
    public static <T> T reflexClass(String className) {
        try {
            Class clz = Class.forName(className);
            return (T) clz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 检查相关的类是否存在
     *
     * @param className 需要检查的类的全路径
     * @return 是否存在
     */
    public static boolean classExists(String className) {
        Class clz = null;
        try {
            clz = Class.forName(className);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clz != null;
    }

}
