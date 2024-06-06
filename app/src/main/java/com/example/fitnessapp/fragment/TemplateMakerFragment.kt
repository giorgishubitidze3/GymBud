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
import com.example.fitnessapp.R
import com.example.fitnessapp.SharedViewModel
import com.example.fitnessapp.adapter.TemplateMakerAdapter
import com.example.fitnessapp.data.WorkoutViewModel


class TemplateMakerFragment : Fragment() {

    private lateinit var viewModel : WorkoutViewModel
    private lateinit var sharedViewModel : SharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_template_maker, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel= ViewModelProvider(requireActivity())[WorkoutViewModel::class.java]
        sharedViewModel= ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        val navController = activity?.findNavController(R.id.fragment_container)

        val context = requireContext()
        val recyclerView = view?.findViewById<RecyclerView>(R.id.add_workout_rvTemplate)
        val adapter = TemplateMakerAdapter(context)

        sharedViewModel.data.observe(viewLifecycleOwner){newData ->
            adapter.setData(newData)
        }

        if (recyclerView != null) {
            recyclerView.adapter = adapter
        }
        if (recyclerView != null) {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }



    }


}