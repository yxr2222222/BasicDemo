package com.yxr.base.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import androidx.core.content.FileProvider
import java.io.File

class PackageUtil {
    companion object {

        private var isDebug: Boolean? = null

        /**
         * 是否是Debug包
         */
        @JvmStatic
        fun isDebug(context: Context): Boolean {
            if (isDebug == null) {
                try {
                    val info = context.applicationInfo
                    isDebug = info.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (isDebug == null) {
                isDebug = false
            }
            return isDebug!!
        }

        @JvmStatic
        fun appIsExist(context: Context?, packageName: String?) =
            getPackageInfo(context, packageName) != null

        /**
         * 获取包名
         *
         * @param context 上下文
         * @return 当前APP包名
         */
        @JvmStatic
        fun getPackageName(context: Context?): String? {
            return context?.packageName
        }

        /**
         * 获取当前APP名称
         *
         * @param context 上下文
         * @return 当前APP名称
         */
        @JvmStatic
        fun getAppName(context: Context): String? {
            val packageInfo = getPackageInfo(context)
            return if (packageInfo == null) null else context.getString(packageInfo.applicationInfo.labelRes)
        }

        /**
         * 获取当前APP版本
         *
         * @param context 上下文
         * @return APP版本
         */
        @JvmStatic
        fun getVersionName(context: Context?): String? {
            val packageInfo = getPackageInfo(context)
            return packageInfo?.versionName
        }

        /**
         * 获取当前APP版本号
         *
         * @param context 上下文
         * @return APP版本号
         */
        @JvmStatic
        fun getVersionCode(context: Context?): Int {
            val packageInfo = getPackageInfo(context)
            return packageInfo?.versionCode ?: -1
        }

        /**
         * 获取当前APP包信息
         *
         * @param context 上下文
         * @return APP包信息
         */
        @JvmStatic
        fun getPackageInfo(context: Context?): PackageInfo? {
            return getPackageInfo(context, getPackageName(context))
        }

        /**
         * 获取指定包名APP包信息
         *
         * @param context     上下文
         * @param packageName 包名
         * @return APP包信息
         */
        @JvmStatic
        fun getPackageInfo(context: Context?, packageName: String?): PackageInfo? {
            if (packageName.isNullOrBlank()) return null
            try {
                val packageManager = getPackageManager(context)
                return packageManager?.getPackageInfo(packageName, 0)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            return null
        }

        /**
         * 获取包管理器
         *
         * @param context 上下文
         * @return 包管理器
         */
        @JvmStatic
        fun getPackageManager(context: Context?): PackageManager? {
            return context?.packageManager
        }

        /**
         * 通过默认的authority（packageName+".fileprovider"）安装APK
         *
         * @param context  上下文
         * @param filePath APK完整路径
         * @return 是否成功唤起安装
         */
        @JvmStatic
        fun installApp(context: Context, filePath: String): Boolean {
            return installApp(context, getPackageName(context) + ".fileprovider", filePath)
        }

        /**
         * 通过authority安装APK
         *
         * @param context   上下文
         * @param filePath  APK完整路径
         * @param authority 指定的FileProvider的authority
         * @return 是否成功唤起安装
         */
        @JvmStatic
        fun installApp(context: Context, authority: String?, filePath: String): Boolean {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val apkUri = FileProvider.getUriForFile(context, authority!!, File(filePath))
                    val install = Intent(Intent.ACTION_VIEW)
                    install.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    install.setDataAndType(apkUri, "application/vnd.android.package-archive")
                    context.startActivity(install)
                } else {
                    val install = Intent(Intent.ACTION_VIEW)
                    install.setDataAndType(
                        Uri.parse("file://$filePath"),
                        "application/vnd.android.package-archive"
                    )
                    install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(install)
                }
                return true
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            return false
        }

        /**
         * shceme跳转
         *
         * @param context 上下文
         * @param url     scheme地址
         * @return 是否唤起跳转
         */
        @JvmStatic
        fun schemeJump(context: Context?, url: String?): Boolean {
            if (context != null && !TextUtils.isEmpty(url)) {
                try {
                    val uri = Uri.parse(url)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                    return true
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            return false
        }

        @JvmStatic
        fun jumpSetting(context: Context) {
            try {
                val intent = Intent()
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                intent.data = Uri.fromParts("package", context.packageName, null)
                context.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}