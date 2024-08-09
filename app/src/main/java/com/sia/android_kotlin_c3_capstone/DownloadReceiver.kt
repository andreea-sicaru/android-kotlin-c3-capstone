package com.sia.android_kotlin_c3_capstone

import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

private val NOTIFICATION_ID = 0

class DownloadReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

        val notificationManager = ContextCompat.getSystemService(
            context, NotificationManager::class.java
        ) as NotificationManager


        val notificationBuilder = NotificationCompat.Builder(
            context,
            context.getString(R.string.download_notification_channel_id)
        )
            .setSmallIcon(R.drawable.icon_download)
            .setContentTitle("Notification Title")
            .setContentText("This is the notification message.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }
}