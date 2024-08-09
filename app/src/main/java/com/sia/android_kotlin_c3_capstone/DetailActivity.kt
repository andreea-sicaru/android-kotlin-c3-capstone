package com.sia.android_kotlin_c3_capstone

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sia.android_kotlin_c3_capstone.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fileName.text = intent.getStringExtra("title")
        binding.status.text = intent.getIntExtra("status", 0).toString()


    }
}
