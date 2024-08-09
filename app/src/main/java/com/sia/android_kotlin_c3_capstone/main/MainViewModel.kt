package com.sia.android_kotlin_c3_capstone.main

import android.app.Application
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import com.sia.android_kotlin_c3_capstone.DownloadReceiver
import com.sia.android_kotlin_c3_capstone.R

class MainViewModel(private val app: Application) : AndroidViewModel(app) {
    private var downloadID: Long = 0
    private val notifyPendingIntent: PendingIntent
    private val REQUEST_CODE = 0


    private val notifyIntent = Intent(app, DownloadReceiver::class.java)

    init {
        createChannel(
            app.resources.getString(R.string.download_notification_channel_id),
            app.resources.getString(R.string.download_notification_channel_description)
        )

        notifyPendingIntent = PendingIntent.getBroadcast(
            getApplication(),
            REQUEST_CODE,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }


    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setShowBadge(false)
            }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description =
                app.resources.getString(R.string.download_notification_channel_description)

            val notificationManager = ContextCompat.getSystemService(
                app.applicationContext, NotificationManager::class.java
            ) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)

        }
    }

    private fun download(option: DownloadOption) {
        Log.d("MainFragment", "Download option: ${option.value}")

        val request = DownloadManager.Request(Uri.parse(option.value))
            .setTitle(app.resources.getString(R.string.app_name))
            .setDescription(app.resources.getString(R.string.app_description))
            .setRequiresCharging(false).setAllowedOverMetered(true).setAllowedOverRoaming(true)

        val downloadManager = ContextCompat.getSystemService(
            app.applicationContext, DownloadManager::class.java
        ) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    fun downloadOptionSelected(checkedRadioButtonId: Int) {
        when (checkedRadioButtonId) {
            R.id.glideRadioButton -> download(DownloadOption.GLIDE)
            R.id.loadAppRadioButton -> download(DownloadOption.LOADAPP)
            R.id.retrofitRadioButton -> download(DownloadOption.RETROFIT)
            else -> Toast.makeText(
                app.applicationContext, "Please select a radio button", Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        enum class DownloadOption(val value: String) {
            GLIDE("https://github.com/bumptech/glide/archive/refs/heads/master.zip"),
            LOADAPP("https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"),
            RETROFIT("https://github.com/square/retrofit/archive/refs/heads/trunk.zip")
        }
    }
}