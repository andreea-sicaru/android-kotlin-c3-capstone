package com.sia.android_kotlin_c3_capstone

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService

private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

class DownloadReceiver(private val listener: Listener) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

        val downloadManager =
            getSystemService(context, DownloadManager::class.java) as DownloadManager
        val query = DownloadManager.Query().setFilterById(id)
        val cursor = downloadManager.query(query)

        var title = ""
        var status = 0
        if (cursor != null && cursor.moveToFirst()) {
            val ids = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID))
            title = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE))
            status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            val uri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI))
            val localUri =
                cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))

            // Process the data
//            Log.d(
//                "DownloadManager",
//                "ID: $ids, Title: $title, Status: $status, URI: $uri, Local URI: $localUri"
//            )
            cursor.close()
        }

//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                val ids = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID))
//                downloadManager.remove(id)
//            } while (cursor.moveToNext())
//        }

        val detailsIntent = Intent(context, DetailActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        detailsIntent.putExtra("title", title)
        detailsIntent.putExtra("status", status)
        val detailsPendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            REQUEST_CODE,
            detailsIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        listener.downloadFinished()
        sendNotification(context, detailsPendingIntent)
    }

    private fun sendNotification(context: Context, detailsPendingIntent: PendingIntent) {
        val notificationManager = getSystemService(
            context, NotificationManager::class.java
        ) as NotificationManager


        val notificationBuilder = NotificationCompat.Builder(
            context,
            context.getString(R.string.download_notification_channel_id)
        )
            .setSmallIcon(R.drawable.icon_download)
            .setContentTitle(context.getString(R.string.download_notification_title))
            .setContentText(context.getString(R.string.download_notification_message))
            .addAction(
                R.drawable.icon_download,
                context.getString(R.string.download_notification_action),
                detailsPendingIntent
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }
}

interface Listener {
    fun downloadFinished()
}
