package com.yxr.base.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.yxr.base.R

class ClipboardUtil {
    companion object {

        /**
         * 获取剪贴板内容
         */
        @JvmStatic
        fun getClipboardData(context: Context?): String? {
            if (context != null) {
                val manager =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                if (manager.hasPrimaryClip() && manager.primaryClip!!.itemCount > 0) {
                    return manager.primaryClip!!.getItemAt(0).text.toString()
                }
            }
            return null
        }

        @JvmStatic
        fun copyDataToClipboard(context: Context, copyData: String?) {
            copyDataToClipboard(context, copyData, true)
        }

        /**
         * 复制内容到剪贴板
         */
        @JvmStatic
        fun copyDataToClipboard(context: Context, copyData: String?, needShowToast: Boolean) {
            try {
                //获取剪贴板管理器：
                val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                // 创建普通字符型ClipData
                val mClipData = ClipData.newPlainText("Label", copyData)
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData)
                if (needShowToast) {
                    ToastUtil.show(context.getString(R.string.copy_success))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}