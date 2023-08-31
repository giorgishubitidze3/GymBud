package com.example.fitnessapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.fitnessapp.databinding.FragmentHomeBinding
import com.example.fitnessapp.databinding.FragmentHomeBinding.inflate


class HomeFragment : Fragment() {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Logging for debugging
        println("HomeFragment: onCreateView")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Logging for debugging
        println("HomeFragment: onViewCreated")

        // You can also set up your UI elements or interactions here if needed
    }
}