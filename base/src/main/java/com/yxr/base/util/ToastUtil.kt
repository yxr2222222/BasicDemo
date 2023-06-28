package com.yxr.base.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import android.widget.Toast
import com.yxr.base.BaseApplication
import java.lang.reflect.Field

class ToastUtil {
    companion object {
        @JvmStatic
        fun show(message: String?) {
            if (!TextUtils.isEmpty(message)) {
                try {
                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
                        SafeToastUtil.show(BaseApplication.context, message)
                    } else {
                        Toast.makeText(BaseApplication.context, message, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }

        @SuppressLint("SoonBlockedPrivateApi")
        object SafeToastUtil {
            private var sField_TN: Field? = null
            private var sField_TN_Handler: Field? = null

            init {
                try {
                    sField_TN = Toast::class.java.getDeclaredField("mTN")
                    val accessible = sField_TN?.setAccessible(true)
                    sField_TN_Handler = sField_TN?.type?.getDeclaredField("mHandler")
                    sField_TN_Handler?.isAccessible = true
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            fun show(context: Context?, message: String?) {
                val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
                hook(toast)
                toast.show()
            }

            private fun hook(toast: Toast) {
                try {
                    val tn = sField_TN!![toast]
                    val preHandler = sField_TN_Handler!![tn] as Handler
                    sField_TN_Handler!![tn] = SafelyHandlerWrapper(preHandler)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            private class SafelyHandlerWrapper internal constructor(private val impl: Handler?) :
                Handler(Looper.getMainLooper()) {
                override fun dispatchMessage(msg: Message) {
                    try {
                        super.dispatchMessage(msg)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun handleMessage(msg: Message) {
                    //需要委托给原Handler执行
                    impl?.handleMessage(msg)
                }
            }
        }
    }
}