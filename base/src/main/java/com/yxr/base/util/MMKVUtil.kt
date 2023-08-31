package com.yxr.base.util

import com.google.gson.Gson
import com.tencent.mmkv.MMKV

class MMKVUtil {
    companion object {

        @JvmStatic
        fun putString(key: String, value: String?) {
            getMMKV().putString(key, value)
        }

        @JvmStatic
        fun getString(key: String, defaultValue: String? = null) =
            getMMKV().getString(key, defaultValue)

        @JvmStatic
        fun putLong(key: String, value: Long) {
            getMMKV().putLong(key, value)
        }

        @JvmStatic
        fun getLong(key: String, defaultValue: Long) = getMMKV().getLong(key, defaultValue)

        @JvmStatic
        fun putFloat(key: String, value: Float) {
            getMMKV().putFloat(key, value)
        }

        @JvmStatic
        fun getFloat(key: String, defaultValue: Float) = getMMKV().getFloat(key, defaultValue)

        @JvmStatic
        fun putBoolean(key: String, value: Boolean) {
            getMMKV().putBoolean(key, value)
        }

        @JvmStatic
        fun getBoolean(key: String, defaultValue: Boolean) = getMMKV().getBoolean(key, defaultValue)

        @JvmStatic
        fun <T> putData(key: String, data: T) {
            try {
                val json = Gson().toJson(data)
                putString(key, json)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        @JvmStatic
        fun <T> getData(key: String, cls: Class<T>, defaultData: T?): T? {
            try {
                val json = getString(key)
                if (json.isNullOrBlank()) {
                    return defaultData
                }
                val data = Gson().fromJson(json, cls)
                return data ?: defaultData
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            return defaultData
        }

        @JvmStatic
        fun getMMKV() = MMKV.defaultMMKV()
    }
}