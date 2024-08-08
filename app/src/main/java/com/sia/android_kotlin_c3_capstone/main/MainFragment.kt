package com.sia.android_kotlin_c3_capstone.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.sia.android_kotlin_c3_capstone.R
import com.sia.android_kotlin_c3_capstone.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)

        binding.downloadButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_mainFragment_to_detailFragment)
        }

        return binding.root
    }
}