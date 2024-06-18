package com.example.fitnessapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessapp.R
import com.example.fitnessapp.adapter.MeasurementsAdapter
import com.example.fitnessapp.databinding.FragmentMeasurementsBinding
import com.example.fitnessapp.databinding.FragmentStatsBinding

class MeasurementsFragment : Fragment() {

    private lateinit var binding: FragmentMeasurementsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMeasurementsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = activity?.findNavController(R.id.fragment_container)

        val recyclerView = binding.recyclerViewMeasurements
        val adapter = MeasurementsAdapter(){selectedMeasurement ->
            val detail = MeasurementsDetailFragment()
            val args = Bundle()
            args.putString("selectedMeasurement", selectedMeasurement)
            detail.arguments = args
            navController?.navigate(R.id.action_measurementsFragment_to_measurementsDetailFragment, args)
        }
        recyclerView.adapter = adapter
    }


}