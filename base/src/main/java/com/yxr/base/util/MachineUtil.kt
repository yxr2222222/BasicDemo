package com.yxr.base.util

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings

class MachineUtil {
    companion object {
        private const val MMKV_DEVICE_ID = "MMKV_DEVICE_ID"

        @SuppressLint("HardwareIds")
        @JvmStatic
        fun getDeviceId(context: Context): String {
            val localDeviceId = MMKVUtil.getString(MMKV_DEVICE_ID, null)
            if (!localDeviceId.isNullOrBlank()) return localDeviceId

            val deviceId =
                Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
                    ?: UUIDUtil.uuid()
            MMKVUtil.putString(MMKV_DEVICE_ID, deviceId)
            return deviceId
        }
    }
}