package com.sia.android_kotlin_c3_capstone

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.registerReceiver
import com.sia.android_kotlin_c3_capstone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), Listener {

    private lateinit var binding: ActivityMainBinding

    private var downloadID: Long = 0

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private val downloadReceiver = DownloadReceiver(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Sets up permissions request launcher for API 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher =
                    registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                        if (isGranted) {
                            Toast.makeText(
                                applicationContext,
                                "Permission granted",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Permission denied",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        registerReceiver(
            applicationContext,
            downloadReceiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
            ContextCompat.RECEIVER_EXPORTED
        )

        binding.downloadButton.setOnClickListener { downloadOptionSelected(binding.radioButtonGroup.checkedRadioButtonId) }

        createNotificationChannel()
    }

    override fun downloadFinished() {
        binding.downloadButton.stopAnimation()
    }

    private fun createNotificationChannel() {
        val channelId = applicationContext.getString(R.string.download_notification_channel_id)
        val channelName = "Download notifications"
        val importance = NotificationManager.IMPORTANCE_HIGH

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, importance)
            channel.description = "Download notifications"

            // Register the channel with the system
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun downloadOptionSelected(checkedRadioButtonId: Int) {
        when (checkedRadioButtonId) {
            R.id.glideRadioButton -> download(DownloadOption.GLIDE)
            R.id.loadAppRadioButton -> download(DownloadOption.LOADAPP)
            R.id.retrofitRadioButton -> download(DownloadOption.RETROFIT)
            else -> Toast.makeText(
                applicationContext, "Please select a radio button", Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun download(option: DownloadOption) {
        val request = DownloadManager.Request(Uri.parse(option.value))
            .setTitle(option.filename())
            .setDescription(resources.getString(R.string.app_description))
            .setRequiresCharging(false)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        val downloadManager = ContextCompat.getSystemService(
            applicationContext, DownloadManager::class.java
        ) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    fun DownloadOption.filename(): String {
        return when (this) {
            DownloadOption.GLIDE -> resources.getString(R.string.glide_image_loading_library_by_bumptech)
            DownloadOption.LOADAPP -> resources.getString(R.string.loadapp_current_repository_by_udacity)
            DownloadOption.RETROFIT -> resources.getString(R.string.retrofit_a_type_safe_http_client_for_android_and_java)
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
