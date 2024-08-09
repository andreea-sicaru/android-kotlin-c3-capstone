package com.sia.android_kotlin_c3_capstone

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class DetailsReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("DetailsReceiver", "onReceive")


    }

}
