package com.example.fitnessapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.MyApplication
import com.example.fitnessapp.R
import com.example.fitnessapp.SharedViewModel
import com.example.fitnessapp.adapter.AddExerciseAdapter
import com.example.fitnessapp.data.GymExercise
import com.example.fitnessapp.data.WorkoutViewModel
import java.util.Locale


class WorkoutPickerFragment : Fragment() {
    private lateinit var workoutViewModel: WorkoutViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_workout_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workoutViewModel = ViewModelProvider(requireActivity())[WorkoutViewModel::class.java]

        val searchBar = view.findViewById<SearchView>(R.id.workoutPickerSearchView)
        //search bar icon
        val searchViewIcon = view.findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)

        val navController = activity?.findNavController(R.id.fragment_container)
        val backButton = view.findViewById<ImageButton>(R.id.back_button)


        workoutViewModel.challengesState.observe(viewLifecycleOwner){state ->
            if (state){
                var data: List<GymExercise> = emptyList()

                val adapter = AddExerciseAdapter(requireContext(),data, viewLifecycleOwner,requireActivity().application as MyApplication, workoutViewModel){ selectedExercise->


//                    val detail = WorkoutDetailsFragment()
//                    val args = Bundle()
//                    args.putParcelable("selectedExercise", selectedExercise)
//                    detail.arguments = args
//                    navController?.navigate(R.id.action_workoutPicker_to_workoutDetails, args)
                }
                val recyclerView = view.findViewById<RecyclerView>(R.id.add_workout_rv)
                recyclerView.adapter = adapter


                val viewModel: SharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]


                viewModel.data.observe(viewLifecycleOwner){newData ->
                    data = newData
                    adapter.setData(data)
                    adapter.notifyDataSetChanged()
                }

                recyclerView.layoutManager = LinearLayoutManager(requireContext())


            }else{
                var data: List<GymExercise> = emptyList()
                val adapter = AddExerciseAdapter(requireContext(),data, viewLifecycleOwner,requireActivity().application as MyApplication, workoutViewModel){ selectedExercise->

                    val detail = WorkoutDetailsFragment()
                    val args = Bundle()
                    args.putParcelable("selectedExercise", selectedExercise)
                    detail.arguments = args
                    navController?.navigate(R.id.action_workoutPicker_to_workoutDetails, args)
                }
                val recyclerView = view.findViewById<RecyclerView>(R.id.add_workout_rv)
                recyclerView.adapter = adapter


                val viewModel: SharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]


                viewModel.data.observe(viewLifecycleOwner){newData ->
                    data = newData
                    adapter.setData(data)
                    adapter.notifyDataSetChanged()
                }

                recyclerView.layoutManager = LinearLayoutManager(requireContext())

                backButton.setOnClickListener {
                    navController?.navigate(R.id.currentSession)
                }




                searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        val list : MutableList<GymExercise> = mutableListOf()

                        if(newText != null){
                            for(i in data){
                                if(i.name.lowercase(Locale.ROOT).contains(newText)){
                                    list.add(i)
                                }
                            }
                        }

                        if(list.isEmpty()){
//                    Toast.makeText(requireContext(),"No data", Toast.LENGTH_SHORT).show()
                            Log.d("WorkoutPicker Fragment" , "No data")
                        }else{
                            adapter.setData(list)
                        }

                        return true
                    }

                })
            }
        }














    }


}