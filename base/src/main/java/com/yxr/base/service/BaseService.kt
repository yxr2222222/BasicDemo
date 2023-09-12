package com.yxr.base.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.os.Build
import androidx.annotation.DrawableRes
import com.yxr.base.R

import com.yxr.base.http.HttpErrorCode
import com.yxr.base.http.extension.launchRequest
import com.yxr.base.http.manager.HttpManager
import com.yxr.base.http.model.IResponse
import com.yxr.base.http.model.NetworkException
import com.yxr.base.http.util.NetworkExceptionUtil
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import okhttp3.Dispatcher

abstract class BaseService : Service() {
    companion object {
        private const val TAG = "BaseService"
        private const val CHANNEL_ID = "com.yxr.base.service.BaseService"
        private const val CHANNEL_NAME = "BaseService"
    }

    private val mainScope = MainScope()

    override fun onDestroy() {
        if (mainScope.isActive) mainScope.cancel()
        super.onDestroy()
    }

    /**
     * 开启前台服服务
     */
    private fun startSimpleForeground(
        channelId: String = CHANNEL_ID,
        channelName: String = CHANNEL_NAME,
        notificationId: Int = 7722,
        @DrawableRes smallIcon: Int = R.drawable.icon_base_notification,
        contentIntent: PendingIntent? = null
    ) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val chan = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_NONE
                )
                val service: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                service.createNotificationChannel(chan)
            }

            val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Notification.Builder(applicationContext, CHANNEL_ID)
            } else {
                Notification.Builder(applicationContext)
            }

            builder.setContentTitle(getString(R.string.app_name))
                .setSmallIcon(smallIcon)
                .setContentText(getString(R.string.app_name))
                .setWhen(System.currentTimeMillis())
            if (contentIntent != null) builder.setContentIntent(contentIntent)
            val notification = builder.build()
            startForeground(notificationId, notification)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    /**
     * 创建RetrofitService，通过Service获取对应api
     */
    private fun <T : Any> createApi(
        cls: Class<T>,
        dispatcher: Dispatcher? = null,
        isJson: Boolean = true
    ): T = HttpManager.get().createApi(cls, dispatcher, isJson)

    /**
     * 快捷请求
     * @param block 需要进行的挂起方法（一般为网络请求之类的）
     * @param onSuccess block方法处理成功回调
     * @param onError block方法处理失败回调
     */
    private fun <T> launchRequest(
        block: suspend () -> IResponse<T>?,
        onSuccess: (T?) -> Unit,
        onError: suspend (exception: NetworkException) -> Unit = {}
    ) {
        if (!mainScope.isActive) return

        mainScope.launchRequest(block, onSuccess = { data ->
            if (data == null) {
                onError(
                    NetworkException(
                        HttpErrorCode.CODE_NULL_DATA,
                        NetworkExceptionUtil.getMessage(com.yxr.base.R.string.default_null_body_exception),
                    )
                )
            } else if (!data.isSuccess()) {
                onError(
                    NetworkException(
                        HttpErrorCode.CODE_UNKNOWN,
                        data.error()
                            ?: NetworkExceptionUtil.getMessage(com.yxr.base.R.string.default_null_body_exception),
                    )
                )
            } else {
                onSuccess(data.getData())
            }
        }, onError = { exception ->
            onError(exception)
        })
    }
}