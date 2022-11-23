package com.example.gameonphone.presentation.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.gameonphone.R
import com.example.gameonphone.databinding.FragmentStartBinding

class StartFragment : Fragment() {

    lateinit var binding: FragmentStartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartBinding.inflate(inflater, container, false)

        val navigate = findNavController()

        binding.buttonStart.setOnClickListener{
            navigate.navigate(R.id.action_startFragment_to_gameFragment)
        }

        return binding.root
    }
}