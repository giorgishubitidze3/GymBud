package com.example.fitnessapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.R
import com.example.fitnessapp.SharedViewModel
import com.example.fitnessapp.adapter.HistoryAdapter
import com.example.fitnessapp.data.WorkoutViewModel
import com.example.fitnessapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var workoutViewModel: WorkoutViewModel

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

        workoutViewModel = ViewModelProvider(requireActivity())[WorkoutViewModel::class.java]


        val recyclerView: RecyclerView = view.findViewById(R.id.historyRecyclerView)
        val adapter = HistoryAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager= LinearLayoutManager(requireContext())

        workoutViewModel.readAllData.observe(viewLifecycleOwner, Observer { workout ->
            adapter.setData(workout)
        } )







    }
}