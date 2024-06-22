package com.example.fitnessapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.fitnessapp.R
import com.example.fitnessapp.adapter.ChallengesAdapter
import com.example.fitnessapp.data.WorkoutViewModel
import com.example.fitnessapp.databinding.FragmentChallengesBinding
import com.example.fitnessapp.databinding.FragmentMeasurementsBinding

class ChallengesFragment : Fragment() {
    private lateinit var binding : FragmentChallengesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChallengesBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = activity?.findNavController(R.id.fragment_container)
        val viewModel: WorkoutViewModel = ViewModelProvider(requireActivity())[WorkoutViewModel::class.java]

        val adapter = ChallengesAdapter()

        binding.addChallengeButton.setOnClickListener {
            viewModel.startChallengesMode()
            navController?.navigate(R.id.action_challengesFragment_to_workoutPicker)
        }
    }

}