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
import com.yxr.base.helper.HttpHelper

abstract class BaseService : Service() {
    companion object {
        private const val CHANNEL_ID = "com.yxr.base.service.BaseService"
        private const val CHANNEL_NAME = "BaseService"
    }

    var httpHelper: HttpHelper? = null
        private set

    override fun onCreate() {
        super.onCreate()
        httpHelper = HttpHelper()
    }

    override fun onDestroy() {
        httpHelper?.release()
        httpHelper = null
        super.onDestroy()
    }


    /**
     * 开启前台服服务
     */
    protected fun startSimpleForeground(
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
}