package com.yxr.base.util

class ClassUtil {
    companion object {

        /**
         * 根据完整路径类名反射获取对象
         *
         * @param className ：完整路径类名
         * @param <T>       ：对象类型
         * @return ：反射对象
        </T> */
        @JvmStatic
        fun <T> reflexClass(className: String): T? {
            try {
                val clz = Class.forName(className)
                return clz.newInstance() as T
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            return null
        }

        /**
         * 检查相关的类是否存在
         *
         * @param className 需要检查的类的全路径
         * @return 是否存在
         */
        @JvmStatic
        fun classExists(className: String): Boolean {
            var clz: Class<*>? = null
            try {
                clz = Class.forName(className)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            return clz != null
        }

        @JvmStatic
        fun <T> getPrivateField(source: Any, fieldName: String): T? {
            try {
                val declaredField = source::class.java.getDeclaredField(fieldName)
                declaredField.isAccessible = true
                return declaredField.get(source) as T?
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            return null
        }
    }
}