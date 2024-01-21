package com.example.fitnessapp.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.MyApplication
import com.example.fitnessapp.R
import com.example.fitnessapp.SharedViewModel
import com.example.fitnessapp.WorkoutDetails
import com.example.fitnessapp.adapter.ExerciseAdapter
import com.example.fitnessapp.data.GymExercise
import com.example.fitnessapp.data.WorkoutViewModel

class WorkoutsFragment : Fragment() {

    private lateinit var workoutViewModel: WorkoutViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_workouts, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workoutViewModel = ViewModelProvider(requireActivity())[WorkoutViewModel::class.java]


        val navController = activity?.findNavController(R.id.fragment_container)

        val workoutDetail = WorkoutDetails()

        var data: List<GymExercise> = emptyList()
        val adapter = ExerciseAdapter(data, requireActivity().application as MyApplication, workoutViewModel){ selectedExercise->

            val detail = WorkoutDetails()
            val args = Bundle()
            args.putParcelable("selectedExercise", selectedExercise)
            detail.arguments = args
            navController?.navigate(R.id.action_workoutsFragment_to_workoutDetails, args)
        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter


        val viewModel: SharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        viewModel.fetchData()

        viewModel.data.observe(viewLifecycleOwner){newData ->
            data = newData
            adapter.setData(data)
            adapter.notifyDataSetChanged()
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

    }
}
