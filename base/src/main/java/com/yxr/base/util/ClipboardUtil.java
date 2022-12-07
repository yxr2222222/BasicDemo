package com.yxr.base.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.yxr.base.R;


public class ClipboardUtil {
    /**
     * 获取剪贴板内容
     */
    public static String getClipboardData(Context context) {
        if (context != null) {
            ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (manager != null) {
                if (manager.hasPrimaryClip() && manager.getPrimaryClip().getItemCount() > 0) {
                    return manager.getPrimaryClip().getItemAt(0).getText().toString();
                }
            }
        }
        return null;
    }

    public static void copyDataToClipboard(Context context, String copyData) {
        copyDataToClipboard(context, copyData, true);
    }

    /**
     * 复制内容到剪贴板
     */
    public static void copyDataToClipboard(Context context, String copyData, boolean needShowToast) {
        try {
            //获取剪贴板管理器：
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm != null) {
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", copyData);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                if (needShowToast) {
                    ToastUtil.show(context, context.getString(R.string.copy_success));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
