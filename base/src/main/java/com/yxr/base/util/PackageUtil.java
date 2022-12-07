package com.yxr.base.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import android.text.TextUtils;

import java.io.File;

/**
 * @author ciba
 * @description Package工具类
 * @date 2020/10/22
 */
public class PackageUtil {
    private static Boolean isDebug = null;

    /**
     * 是否是Debug包
     */
    public static boolean isDebug(Context context) {
        if (isDebug == null) {
            try {
                ApplicationInfo info = context.getApplicationInfo();
                isDebug = (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        if (isDebug == null) {
            isDebug = false;
        }
        return isDebug;
    }

    /**
     * 获取包名
     *
     * @param context 上下文
     * @return 当前APP包名
     */
    public static String getPackageName(Context context) {
        if (context == null) {
            return null;
        }
        return context.getPackageName();
    }

    /**
     * 获取当前APP名称
     *
     * @param context 上下文
     * @return 当前APP名称
     */
    public static String getAppName(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        return packageInfo == null ? null : context.getString(packageInfo.applicationInfo.labelRes);
    }

    /**
     * 获取当前APP版本
     *
     * @param context 上下文
     * @return APP版本
     */
    public static String getVersionName(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        return packageInfo == null ? null : packageInfo.versionName;
    }

    /**
     * 获取当前APP版本号
     *
     * @param context 上下文
     * @return APP版本号
     */
    public static int getVersionCode(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        return packageInfo == null ? -1 : packageInfo.versionCode;
    }

    /**
     * 获取当前APP包信息
     *
     * @param context 上下文
     * @return APP包信息
     */
    public static PackageInfo getPackageInfo(Context context) {
        return getPackageInfo(context, getPackageName(context));
    }

    /**
     * 获取指定包名APP包信息
     *
     * @param context     上下文
     * @param packageName 包名
     * @return APP包信息
     */
    public static PackageInfo getPackageInfo(Context context, String packageName) {
        try {
            PackageManager packageManager = getPackageManager(context);
            return packageManager == null ? null : packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取包管理器
     *
     * @param context 上下文
     * @return 包管理器
     */
    public static PackageManager getPackageManager(Context context) {
        return context == null ? null : context.getPackageManager();
    }

    /**
     * 通过默认的authority（packageName+".fileprovider"）安装APK
     *
     * @param context  上下文
     * @param filePath APK完整路径
     * @return 是否成功唤起安装
     */
    public static boolean installApp(Context context, String filePath) {
        return installApp(context, getPackageName(context) + ".fileprovider", filePath);
    }

    /**
     * 通过authority安装APK
     *
     * @param context   上下文
     * @param filePath  APK完整路径
     * @param authority 指定的FileProvider的authority
     * @return 是否成功唤起安装
     */
    public static boolean installApp(Context context, String authority, String filePath) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Uri apkUri = FileProvider.getUriForFile(context, authority, new File(filePath));
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                install.setDataAndType(apkUri, "application/vnd.android.package-archive");
                context.startActivity(install);
            } else {
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(install);
            }
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * shceme跳转
     *
     * @param context 上下文
     * @param url     scheme地址
     * @return 是否唤起跳转
     */
    public static boolean schemeJump(Context context, String url) {
        if (context != null && !TextUtils.isEmpty(url)) {
            try {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                return true;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void jumpSetting(Activity activity) {
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
