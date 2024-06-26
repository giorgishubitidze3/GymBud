package com.example.fitnessapp.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.R
import com.example.fitnessapp.adapter.TemplateAdapter
import com.example.fitnessapp.data.WorkoutViewModel

class SessionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_session, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel: WorkoutViewModel = ViewModelProvider(requireActivity()).get(WorkoutViewModel::class.java)
        val startWorkoutButton = view.findViewById<Button>(R.id.buttonSessionStart)

        val navController = activity?.findNavController(R.id.fragment_container)
        val currentSessionContainer = view.findViewById<ConstraintLayout>(R.id.currentSessionContainer)
        val currentTimer = view.findViewById<TextView>(R.id.currentSessionTimer)

        val addRoutineBtn = view.findViewById<ImageButton>(R.id.addRoutineBtn)

        val currentSessionNameTV = view.findViewById<TextView>(R.id.currentSessionName)

        val templateRecyclerView = view.findViewById<RecyclerView>(R.id.routinesRecyclerView)
        val templateAdapter = navController?.let {
            TemplateAdapter(requireContext(), viewModel,
                it, viewLifecycleOwner
            )
        }




        val alertDialogBuilder = AlertDialog.Builder(requireContext())
            .setTitle("Warning")
            .setMessage("Are you sure you want to start a new workout?")
            .setPositiveButton(android.R.string.yes) { _, _ ->
                viewModel.stopTimer()
                viewModel.endWorkout()
                viewModel.resetCurrentSets()
                viewModel.resetCurrentWorkouts()
                Log.d("ALERTBOXMAIN", "yes clicked")
                viewModel.resetCurrentRoutineName()
                navController?.navigate(R.id.currentSession)
                viewModel.startWorkout()
                viewModel.startTimer()
            }
            .setNegativeButton(android.R.string.no) { _, _ ->
                Toast.makeText(requireContext(), android.R.string.no, Toast.LENGTH_SHORT).show()
            }


        viewModel.workoutState.observe(viewLifecycleOwner) { isWorkoutActive ->
            currentSessionContainer.visibility = if (isWorkoutActive) View.VISIBLE else View.GONE
        }


        fun showEditTextDialog() {
            val builder = AlertDialog.Builder(requireContext())
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.edit_text_dialog_layout, null)
            val editText = dialogLayout.findViewById<EditText>(R.id.et_dialogEditText)

            builder.setTitle("Enter workout name")
                .setPositiveButton("OK") { _, _ ->
                    val newRoutineName = editText.text.toString()
                    if (newRoutineName.isNotBlank()) {
                        if (viewModel.workoutState.value == true) {
                            alertDialogBuilder.setMessage("Are you sure you want to change the current workout name?")
                                .setPositiveButton(android.R.string.yes) { _, _ ->
                                    viewModel.changeRoutineName(newRoutineName)
                                    viewModel.resetCurrentWorkouts()
                                    viewModel.resetCurrentSets()
                                    navController?.navigate(R.id.currentSession)

                                }
                                .show()
                        } else {
                            navController?.navigate(R.id.currentSession)
                            viewModel.startWorkout()
                            viewModel.startTimer()
                            viewModel.resetCurrentWorkouts()
                            viewModel.resetCurrentSets()
                            viewModel.changeRoutineName(newRoutineName)
                        }
                    }
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .setView(dialogLayout)
                .show()
        }


        val alertDialogBuilderTemplate = AlertDialog.Builder(requireContext())
            .setTitle("Warning")
            .setMessage("Are you sure you want to end the current session")
            .setPositiveButton(android.R.string.yes) { _, _ ->
                viewModel.stopTimer()
                viewModel.endWorkout()
                viewModel.resetCurrentSets()
                viewModel.resetCurrentWorkouts()
                Log.d("ALERTBOXMAIN", "yes clicked")
                viewModel.resetCurrentRoutineName()
                navController?.navigate(R.id.currentSession)
                viewModel.startTemplateMaker()
            }
            .setNegativeButton(android.R.string.no) { _, _ ->
                Toast.makeText(requireContext(), android.R.string.no, Toast.LENGTH_SHORT).show()
            }


        addRoutineBtn.setOnClickListener {
            //showEditTextDialog()
            viewModel.workoutState.observe(viewLifecycleOwner){state->

                if(state){
                    alertDialogBuilderTemplate.show()
                }else{
                    viewModel.startTemplateMaker()
                    navController?.navigate(R.id.currentSession)
                }
            }
        }


        viewModel.currentTemplatesWithSets.observe(viewLifecycleOwner){item ->
            templateAdapter?.setData(item)
        }

        templateRecyclerView.adapter =templateAdapter

        templateRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        viewModel.currentRoutineName.observe(viewLifecycleOwner){
            currentSessionNameTV.text = it
        }


        //START BUTTON
        startWorkoutButton.setOnClickListener {
            var firstState = false
            viewModel.workoutState.observe(viewLifecycleOwner){
                state ->
                firstState = state

            }
            if (firstState){
                alertDialogBuilder.show()
            }else{
//                navController?.navigate(R.id.currentSession)
                navController?.navigate(R.id.action_sessionFragment_to_currentSession)
                viewModel.resetCurrentRoutineName()
                //openNewSessionFragment()
                viewModel.startWorkout()
                viewModel.startTimer()
                viewModel.resetCurrentWorkouts()
                viewModel.resetCurrentSets()
            }
        }


        viewModel.elapsedtime.observe(viewLifecycleOwner) { formattedTime ->
            currentTimer.text = formattedTime
        }


        currentSessionContainer.setOnClickListener{
//           navController?.navigate(R.id.currentSession)
            navController?.navigate(R.id.action_sessionFragment_to_currentSession)
        }


    }

    private fun openNewSessionFragment() {
        val currentSessionFragment = CurrentSessionFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()

        // Replace the current fragment with the NewSessionFragment
        transaction.add(R.id.fragment_container, currentSessionFragment, "currentSession")
        transaction.addToBackStack("currentSession")
        transaction.commit()
    }
}
