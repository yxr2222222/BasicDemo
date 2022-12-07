package com.yxr.base.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.yxr.base.BaseApplication;

import java.lang.reflect.Field;

/**
 * @author ciba
 * @description 安全的Toast工具类
 * @date 2020/09/17
 */
public class ToastUtil {
    public static void show(String message) {
        show(BaseApplication.context, message);
    }

    public static void show(Context context, String message) {
        if (context != null && !TextUtils.isEmpty(message)) {
            context = context.getApplicationContext();
            try {
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
                    SafeToastUtil.show(context, message);
                } else {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("SoonBlockedPrivateApi")
    public static class SafeToastUtil {
        private static Field sField_TN;
        private static Field sField_TN_Handler;

        static {
            try {
                sField_TN = Toast.class.getDeclaredField("mTN");
                sField_TN.setAccessible(true);
                sField_TN_Handler = sField_TN.getType().getDeclaredField("mHandler");
                sField_TN_Handler.setAccessible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void show(Context context, String message) {
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            hook(toast);
            toast.show();
        }

        private static void hook(Toast toast) {
            try {
                Object tn = sField_TN.get(toast);
                Handler preHandler = (Handler) sField_TN_Handler.get(tn);
                sField_TN_Handler.set(tn, new SafelyHandlerWrapper(preHandler));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private static class SafelyHandlerWrapper extends Handler {

            private Handler impl;

            SafelyHandlerWrapper(Handler impl) {
                this.impl = impl;
            }

            @Override
            public void dispatchMessage(Message msg) {
                try {
                    super.dispatchMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void handleMessage(Message msg) {
                //需要委托给原Handler执行
                if (impl != null) {
                    impl.handleMessage(msg);
                }
            }
        }
    }
}
