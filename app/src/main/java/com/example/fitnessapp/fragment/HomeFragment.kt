package com.example.fitnessapp.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.fitnessapp.SharedViewModel
import com.example.fitnessapp.data.GymExercise
import com.example.fitnessapp.data.WorkoutViewModel
import com.example.fitnessapp.databinding.FragmentHomeBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.util.Calendar


class HomeFragment : Fragment() {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var workoutViewModel: WorkoutViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        binding = FragmentHomeBinding.inflate(inflater, container, false)




        workoutViewModel = ViewModelProvider(requireActivity())[WorkoutViewModel::class.java]
        workoutViewModel.currentUserName.observe(viewLifecycleOwner) { name ->
            binding.homeUsernameTv.text = name
            binding.homeUsernameTv.visibility = View.VISIBLE
        }


        // Logging for debugging
        println("HomeFragment: onCreateView")

        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        fun readJsonFromAssets(context: Context, fileName:String): String{
            return context.assets.open(fileName).bufferedReader().use{it.readText()}
        }

        fun parseJsonData(jsonString: String): List<GymExercise>{
            val gson = Gson()
            return gson.fromJson(jsonString, object : TypeToken<List<GymExercise>>() {}.type)
        }

        workoutViewModel = ViewModelProvider(requireActivity())[WorkoutViewModel::class.java]

        //parse json
        val jsonString = readJsonFromAssets(requireContext(),"main.json")
        val parsedGymExercises = parseJsonData(jsonString)



        Log.d("HomeFragment", "parsed item count is ${parsedGymExercises.count()}")


        sharedViewModel.updateData(parsedGymExercises)


    }
}