package com.yxr.base.util

import android.content.ComponentName
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import androidx.core.content.FileProvider
import com.yxr.base.BaseApplication
import com.yxr.base.BasicClient
import java.io.File

class PackageUtil {
    companion object {

        private var isDebug: Boolean? = null

        /**
         * 是否是Debug包
         */
        @JvmStatic
        fun isDebug(): Boolean {
            if (isDebug == null) {
                try {
                    val info = BaseApplication.context.applicationInfo
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
        fun appIsExist(packageName: String?) =
            getPackageInfo(packageName) != null

        /**
         * 获取包名
         *
         * @return 当前APP包名
         */
        @JvmStatic
        fun getPackageName(): String? {
            return BaseApplication.context.packageName
        }

        /**
         * 获取当前APP名称
         *
         * @param context 上下文
         * @return 当前APP名称
         */
        @JvmStatic
        fun getAppName(): String? {
            val packageInfo = getPackageInfo()
            return if (packageInfo == null) null else BaseApplication.context.getString(packageInfo.applicationInfo.labelRes)
        }

        /**
         * 获取当前APP版本
         *
         * @return APP版本
         */
        @JvmStatic
        fun getVersionName(): String? {
            val packageInfo = getPackageInfo()
            return packageInfo?.versionName
        }

        /**
         * 获取当前APP版本号
         *
         * @return APP版本号
         */
        @JvmStatic
        fun getVersionCode(): Int {
            val packageInfo = getPackageInfo()
            return packageInfo?.versionCode ?: -1
        }

        /**
         * 获取当前APP包信息
         *
         * @return APP包信息
         */
        @JvmStatic
        fun getPackageInfo(): PackageInfo? {
            return getPackageInfo( getPackageName())
        }

        /**
         * 获取指定包名APP包信息
         *
         * @param context     上下文
         * @param packageName 包名
         * @return APP包信息
         */
        @JvmStatic
        fun getPackageInfo(packageName: String?): PackageInfo? {
            if (packageName.isNullOrBlank()) return null
            try {
                val packageManager = getPackageManager()
                return packageManager?.getPackageInfo(packageName, 0)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            return null
        }

        /**
         * 获取包管理器
         *
         * @return 包管理器
         */
        @JvmStatic
        fun getPackageManager(): PackageManager? {
            return BaseApplication.context.packageManager
        }

        /**
         * 通过默认的authority（packageName+".fileprovider"）安装APK
         *
         * @param filePath APK完整路径
         * @return 是否成功唤起安装
         */
        @JvmStatic
        fun installApp(filePath: String): Boolean {
            return installApp(getPackageName() + ".fileprovider", filePath)
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
        fun installApp(authority: String?, filePath: String): Boolean {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val apkUri = FileProvider.getUriForFile(BaseApplication.context, authority!!, File(filePath))
                    val install = Intent(Intent.ACTION_VIEW)
                    install.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    install.setDataAndType(apkUri, "application/vnd.android.package-archive")
                    BaseApplication.context.startActivity(install)
                } else {
                    val install = Intent(Intent.ACTION_VIEW)
                    install.setDataAndType(
                        Uri.parse("file://$filePath"),
                        "application/vnd.android.package-archive"
                    )
                    install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    BaseApplication.context.startActivity(install)
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
         * @param url     scheme地址
         * @return 是否唤起跳转
         */
        @JvmStatic
        fun schemeJump(url: String?): Boolean {
            if (!TextUtils.isEmpty(url)) {
                try {
                    val uri = Uri.parse(url)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    BaseApplication.context.startActivity(intent)
                    return true
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            return false
        }

        @JvmStatic
        fun jumpWechatScan(){
            try {
                // 调用微信扫一扫
                val intent = Intent()
                intent.component =
                    ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI")
                intent.putExtra("LauncherUI.From.Scaner.Shortcut", true)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.action = "android.intent.action.VIEW"
                BasicClient.instance.context.startActivity(intent)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        @JvmStatic
        fun jumpSetting() {
            try {
                val intent = Intent()
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                intent.data = Uri.fromParts("package", BaseApplication.context.packageName, null)
                BaseApplication.context.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}