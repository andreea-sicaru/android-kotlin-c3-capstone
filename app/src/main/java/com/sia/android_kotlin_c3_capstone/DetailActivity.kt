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
        val status = intent.getIntExtra("status", 0)
        when(status){
            1 -> {
                binding.status.text = getString(R.string.status_pending)
            }
            2 -> {
                binding.status.text = getString(R.string.status_running)
            }
            4 -> {
                binding.status.text = getString(R.string.status_paused)
            }
            8 -> {
                binding.status.text = getString(R.string.status_success)
            }
            16 -> {
                binding.status.text = getString(R.string.status_fail)
                binding.status.setTextColor(getColor(R.color.colorRed))
            }

        }


        binding.okButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
