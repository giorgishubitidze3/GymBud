package com.example.fitnessapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.MyApplication
import com.example.fitnessapp.R
import com.example.fitnessapp.SharedViewModel
import com.example.fitnessapp.WorkoutDetails
import com.example.fitnessapp.adapter.AddExerciseAdapter
import com.example.fitnessapp.data.GymExercise
import com.example.fitnessapp.data.WorkoutViewModel


class WorkoutPickerFragment : Fragment() {
    private lateinit var workoutViewModel: WorkoutViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_workout_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workoutViewModel = ViewModelProvider(requireActivity())[WorkoutViewModel::class.java]

        val navController = activity?.findNavController(R.id.fragment_container)
        val backButton = view.findViewById<ImageButton>(R.id.back_button)
        var data: List<GymExercise> = emptyList()
        val adapter = AddExerciseAdapter(requireContext(),data, requireActivity().application as MyApplication, workoutViewModel){ selectedExercise->

            val detail = WorkoutDetails()
            val args = Bundle()
            args.putParcelable("selectedExercise", selectedExercise)
            detail.arguments = args
            navController?.navigate(R.id.action_workoutPicker_to_workoutDetails, args)
        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.add_workout_rv)
        recyclerView.adapter = adapter


        val viewModel: SharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        viewModel.fetchData()

        viewModel.data.observe(viewLifecycleOwner){newData ->
            data = newData
            adapter.setData(data)
            adapter.notifyDataSetChanged()
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        backButton.setOnClickListener {
            navController?.navigate(R.id.currentSession)
        }


    }


}