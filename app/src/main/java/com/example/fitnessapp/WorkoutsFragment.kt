package com.example.fitnessapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.data.GymExercise
import com.example.fitnessapp.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.http2.Http2Reader.Companion.logger

class WorkoutsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_workouts, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val workoutDetail = WorkoutDetails()

        var data: List<GymExercise> = emptyList()
        val adapter = ExerciseAdapter(data){ ->
           Navigation.findNavController(requireView()).navigate(R.id.action_workoutsFragment_to_workoutDetails)
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
