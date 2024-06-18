package com.example.fitnessapp.fragment


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.MyApplication
import com.example.fitnessapp.R
import com.example.fitnessapp.SharedViewModel
import com.example.fitnessapp.adapter.ExerciseAdapter
import com.example.fitnessapp.data.GymExercise
import com.example.fitnessapp.data.WorkoutViewModel
import java.util.Locale

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

        val workoutDetail = WorkoutDetailsFragment()

        val workoutsTextView = view.findViewById<TextView>(R.id.workoutsTextView)
        val searchView = view.findViewById<SearchView>(R.id.searchView)
        val searchAutoComplete = searchView.findViewById<SearchView.SearchAutoComplete>(androidx.appcompat.R.id.search_src_text)

        var data: List<GymExercise> = emptyList()
        val adapter = ExerciseAdapter(requireContext(),data, requireActivity().application as MyApplication, workoutViewModel){ selectedExercise->

            val detail = WorkoutDetailsFragment()
            val args = Bundle()
            args.putParcelable("selectedExercise", selectedExercise)
            detail.arguments = args
            navController?.navigate(R.id.action_workoutsFragment_to_workoutDetails, args)
        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter


        //WORKOUTS text visibility based on searchview focus
        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                workoutsTextView.visibility = View.GONE
            } else{
                workoutsTextView.visibility = View.VISIBLE
            }
        }

        searchView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            val isIconified = searchView.isIconified
            if (isIconified) {
                workoutsTextView.visibility = View.VISIBLE
            } else {
                workoutsTextView.visibility = View.GONE
            }
        }


        //iconify searchview on back button press
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!searchView.isIconified) {
                    searchView.setIconified(true)
                    searchView.onActionViewCollapsed()
                    searchView.clearFocus()
                    workoutsTextView.visibility = View.VISIBLE
                } else {
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        })

        searchAutoComplete.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                searchView.setIconified(true)
                searchView.onActionViewCollapsed()
                 workoutsTextView.visibility = View.VISIBLE
            }
        }




        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
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
//                    Toast.makeText(requireContext(),"No data",Toast.LENGTH_SHORT).show()
                    Log.d("WorkoutFragment","No data")
                }else{
                    adapter.setData(list)
                }

                return true
            }

        })


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
