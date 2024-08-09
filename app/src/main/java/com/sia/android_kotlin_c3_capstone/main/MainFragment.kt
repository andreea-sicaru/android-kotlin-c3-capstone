package com.sia.android_kotlin_c3_capstone.main

import android.app.DownloadManager
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.registerReceiver
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sia.android_kotlin_c3_capstone.DownloadReceiver
import com.sia.android_kotlin_c3_capstone.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)

        binding.viewModel = viewModel

        binding.downloadButton.setOnClickListener { view: View ->
            viewModel.downloadOptionSelected(binding.radioButtonGroup.checkedRadioButtonId)
        }

        registerReceiver(
            requireContext(),
            DownloadReceiver(),
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
            ContextCompat.RECEIVER_EXPORTED
        )

        return binding.root
    }
}