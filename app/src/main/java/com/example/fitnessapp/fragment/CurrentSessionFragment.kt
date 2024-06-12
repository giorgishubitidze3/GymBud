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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.R
import com.example.fitnessapp.adapter.CurrentSessionAdapter
import com.example.fitnessapp.data.GymExercise
import com.example.fitnessapp.data.Template
import com.example.fitnessapp.data.TemplateSet
import com.example.fitnessapp.data.TemplateWithSets
import com.example.fitnessapp.data.WorkoutViewModel


class CurrentSessionFragment : Fragment() {
    private lateinit var viewModel: WorkoutViewModel
    private lateinit var cancelDialogBuilder: AlertDialog.Builder
    private lateinit var finishDialogBuilder: AlertDialog.Builder
    private lateinit var cancelDialogBuilderTemplate: AlertDialog.Builder
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_current_session, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cancelDialogBuilder = AlertDialog.Builder(requireContext())
        finishDialogBuilder = AlertDialog.Builder(requireContext())
        cancelDialogBuilderTemplate = AlertDialog.Builder(requireContext())

        viewModel= ViewModelProvider(requireActivity())[WorkoutViewModel::class.java]
        val finishButton = view.findViewById<Button>(R.id.buttonFinish)
        val navController = activity?.findNavController(R.id.fragment_container)
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        val buttonCancel = view.findViewById<Button>(R.id.btnCancel)
        val buttonAdd = view.findViewById<Button>(R.id.btnAdd)
        val routineNameTV = view.findViewById<TextView>(R.id.routineName)
        val editRoutineNameBtn = view.findViewById<ImageButton>(R.id.editRoutineName)
        val slideDownButton = view.findViewById<ImageButton>(R.id.slide_down_button)
        val currentTimer = view.findViewById<TextView>(R.id.currentTimer)

        var listCurrentWorkouts = emptyList<GymExercise>()

        val exercisesAdapter = CurrentSessionAdapter(viewModel, viewLifecycleOwner, requireContext())

        cancelDialogBuilder.setTitle("Cancel Workout")
        cancelDialogBuilder.setMessage("Are you sure you want to cancel this workout?")


        //finishButton
        //alertBoxYES
        cancelDialogBuilder.setPositiveButton(android.R.string.yes){  _, _ ->
            navController?.navigate(R.id.sessionFragment)
            viewModel.stopTimer()
            viewModel.endWorkout()
//                exercisesAdapter.clearData()
            Log.d("ALERTBOX","yes clicked")

        }

        cancelDialogBuilder.setNegativeButton(android.R.string.no) { _, _  ->
            Toast.makeText(requireContext(),
                android.R.string.no, Toast.LENGTH_SHORT).show()
        }


        cancelDialogBuilderTemplate.setTitle("Cancel Workout")
        cancelDialogBuilderTemplate.setMessage("Are you sure you want to cancel this workout?")
        cancelDialogBuilderTemplate.setPositiveButton(android.R.string.yes){  _, _ ->
            navController?.navigate(R.id.sessionFragment)
            viewModel.stopTimer()
            viewModel.endWorkout()
            viewModel.endTemplateMaker()
            viewModel.resetCurrentWorkouts()
            viewModel.resetCurrentSets()
            Log.d("ALERTBOX","yes clicked")

        }

        cancelDialogBuilderTemplate.setNegativeButton(android.R.string.no) { _, _  ->
            Toast.makeText(requireContext(),
                android.R.string.no, Toast.LENGTH_SHORT).show()
        }



        val incompleteSetsDialog = AlertDialog.Builder(requireContext())

        with(incompleteSetsDialog){
            setTitle("Incomplete sets will not be saved")
            setMessage("Do you want to end the session anyway ?")
            setPositiveButton("YES"){_,_ ->
                val completedSets = viewModel.getAllCompletedSets()
                viewModel.insertRoutineWithSets(
                    viewModel.getRoutineObj(),
                    completedSets
                )
            }

            setNegativeButton("NO"){_ , _ ->

            }

        }


        val emptyWorkoutDialog = AlertDialog.Builder(requireContext())

        with(emptyWorkoutDialog){
            setTitle("Empty workout won't be saved")
            setMessage("Do you want to continue anyway?")
            setPositiveButton("YES"){_,_ ->
                viewModel.resetCurrentWorkouts()
                viewModel.resetCurrentRoutineName()
                viewModel.resetCurrentSets()
                viewModel.endTemplateMaker()
                viewModel.endTemplateEditor()
                viewModel.endWorkout()
                viewModel.stopTimer()
                viewModel.resetCurrentTemplateSets()

                navController?.navigate(R.id.sessionFragment)
            }
            setNegativeButton("NO"){_,_ ->
                Log.d("CurrentSessionFragment", "pressed no on emptyWorkoutDialog")
            }
        }



        finishDialogBuilder.setTitle("Alert")
        finishDialogBuilder.setMessage("Are you sure you want to finish this workout?")

        finishDialogBuilder.setPositiveButton(android.R.string.yes){  _, _ ->
            navController?.navigate(R.id.sessionFragment)
            viewModel.stopTimer()
            viewModel.endWorkout()

            val completedSets = viewModel.getAllCompletedSets()

            if(completedSets.any{ !it.isCompleted}){
                incompleteSetsDialog.show()
            }else{
                viewModel.insertRoutineWithSets(
                    viewModel.getRoutineObj(),
                    completedSets
                )
            }



//            exercisesAdapter.clearData()
            Log.d("ALERTBOX","yes clicked")

        }

        val finishTemplateEditorDialog = AlertDialog.Builder(requireContext())

        with(finishTemplateEditorDialog){
            setTitle("Alert")
            setMessage("Do you want to finish editing the template?")
            setPositiveButton("YES"){_,_ ->
                navController?.navigate(R.id.sessionFragment)
                viewModel.currentSets.value?.let { it1 ->
                    viewModel.insertTemplateWithSets(viewModel.getTemplateObj(),
                        it1
                    )
                }
                viewModel.resetCurrentSets()
                viewModel.resetCurrentWorkouts()
                viewModel.resetCurrentRoutineName()
                viewModel.endTemplateMaker()
            }
            setNegativeButton("NO"){_,_ ->

            }
        }


        //alertBoxNO
        finishDialogBuilder.setNegativeButton(android.R.string.no) { _, _  ->
            Toast.makeText(requireContext(),
                android.R.string.no, Toast.LENGTH_SHORT).show()
        }


        val template = viewModel.getAllTemplates(viewModel.currentUserId)
        Log.d("CurrentSessionFragment","template list ${template.value.toString()}")

        var filteredTemplate : List<Template> = emptyList()

        template.observe(viewLifecycleOwner){template ->
            if(template.isNullOrEmpty()){
                Log.d("CurrentSessionFragment","filteredtemplates is still empty")
            }else{
                Log.d("CurrentSessionFragment","filteredtemplates is not empty")
                viewModel.currentRoutineName.observe(viewLifecycleOwner){currentName ->

                    filteredTemplate = template.filter{it.name == currentName}
                    Log.d("CurrentSessionFragment","$filteredTemplate  heres filteredTemplateList")
                }
            }
        }



        viewModel.templateState.observe(viewLifecycleOwner) { state ->
            if (state) {
                finishButton.text = "Save"
                currentTimer.visibility = View.GONE
                slideDownButton.visibility = View.GONE

                finishButton.setOnClickListener {
                    viewModel.currentSets.observe(viewLifecycleOwner){
                        if(it.isEmpty()){
                            emptyWorkoutDialog.show()
                        }else{
                            if(filteredTemplate.any { filteredTemplate -> filteredTemplate.name == viewModel.currentRoutineName.value.toString() }){
                                val templateAlreadyExistsDialog = AlertDialog.Builder(requireContext())

                                with(templateAlreadyExistsDialog){
                                    setTitle("Alert")
                                    setMessage("Template already exists with this name, do you want to update it?")
                                    setPositiveButton("YES"){_,_  ->
                                        val template = filteredTemplate[0]
                                        val templateId = template.templateId
                                        Log.d("templateSets", "${templateId.toString()} this is templateId in templateState")

                                        var templateSets: MutableList<TemplateSet> = mutableListOf()

                                        viewModel.currentSets.observe(viewLifecycleOwner) { currentSets ->
                                            if(currentSets!=null) {

                                                currentSets.forEach { set ->
                                                    templateSets.add(viewModel.workoutSetToTemplateSet(set, templateId))
                                                }
                                                templateSets.forEach { set -> Log.d("templateSets", set.templateId.toString() ) }
                                            }
                                        }

                                        viewModel.updateTemplate(
                                            templateId,
                                            viewModel.currentRoutineName.value.toString(),
                                            templateSets.toList()
                                        )

                                        navController?.navigate(R.id.sessionFragment, null, NavOptions.Builder()
                                            .setPopUpTo(R.id.currentSession, true)
                                            .build())

                                        viewModel.resetCurrentSets()
                                        viewModel.resetCurrentWorkouts()
                                        viewModel.resetCurrentRoutineName()
                                        viewModel.endTemplateMaker()

                                    }

                                    setNegativeButton("NO"){_,_ ->

                                    }
                                    show()
                                }
                            }else{
                                navController?.navigate(R.id.sessionFragment)
                                viewModel.currentSets.value?.let { it1 ->
                                    viewModel.insertTemplateWithSets(viewModel.getTemplateObj(),
                                        it1
                                    )
                                }
                                viewModel.resetCurrentSets()
                                viewModel.resetCurrentWorkouts()
                                viewModel.resetCurrentRoutineName()
                                viewModel.endTemplateMaker()
                            }
                        }
                    }

                }

                buttonCancel.setOnClickListener {
                    //TODO modify later
                    cancelDialogBuilderTemplate.show()
                }
            } else {
                viewModel.templateEditState.observe(viewLifecycleOwner) { isEditing ->
                    if (isEditing) {
                        finishButton.text = "Update"
                        currentTimer.visibility = View.GONE
                        slideDownButton.visibility = View.GONE


                        val templateId = viewModel.currentTemplateSets.value?.get(0)?.templateId ?: 0
                        Log.d("templateSets", "${templateId.toString()} this is templateId in fragment")

                        finishButton.setOnClickListener {

                            var templateSets: MutableList<TemplateSet> = mutableListOf()



                            viewModel.currentSets.observe(viewLifecycleOwner) { currentSets ->
                                if(currentSets!=null) {

                                    currentSets.forEach { set ->
                                        templateSets.add(viewModel.workoutSetToTemplateSet(set, templateId))
                                    }
                                    templateSets.forEach { set -> Log.d("templateSets", set.templateId.toString() ) }
                                }
                            }


                            if(viewModel.currentSets.value?.isEmpty() == true){
                                emptyWorkoutDialog.show()
                            }else{
                                viewModel.updateTemplate(
                                    templateId,
                                    viewModel.currentRoutineName.value.toString(),
                                    templateSets.toList()
                                )

                                navController?.navigate(R.id.sessionFragment)
                                viewModel.resetCurrentSets()
                                viewModel.resetCurrentWorkouts()
                                viewModel.resetCurrentRoutineName()
                                viewModel.endTemplateEditor()
                            }
                        }

                        buttonCancel.setOnClickListener {
                            //TODO modify later
                            cancelDialogBuilderTemplate.show()
                        }
                    } else {
                        finishButton.text = "Finish"
                        currentTimer.visibility = View.VISIBLE
                        slideDownButton.visibility = View.VISIBLE
                        finishButton.setOnClickListener {
                            Log.d("currentSessionFragmentDebug", "Finish Button pressed")
                            if(viewModel.currentSets.value?.isEmpty() == true){
                                emptyWorkoutDialog.show()
                            }else{
                                finishDialogBuilder.show()
                            }

                            Toast.makeText(requireContext(), "Routine inserted", Toast.LENGTH_SHORT)
                                .show()
                        }

                        buttonCancel.setOnClickListener {
                            //TODO modify later
                            cancelDialogBuilder.show()
                            viewModel.resetCurrentWorkouts()
                            viewModel.resetCurrentRoutineName()
                            viewModel.resetCurrentSets()
                        }
                    }

                }

            }
        }

        viewModel.currentWorkouts.observe(viewLifecycleOwner){
            Log.d("Observer", "Called")
            exercisesAdapter.setData(it)
            listCurrentWorkouts = it
        }


        viewModel.currentRoutineName.observe(viewLifecycleOwner){
            routineNameTV.text = it
        }


        fun showEditTextDialog(){
            val builder = AlertDialog.Builder(requireContext())
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.edit_text_dialog_layout, null)
            val editText = dialogLayout.findViewById<EditText>(R.id.et_dialogEditText)

            with(builder){
                setTitle("Enter workout name")
                setPositiveButton("OK"){dialog, which ->
                    if (editText.text.toString().isNotBlank()){
                   viewModel.changeRoutineName(editText.text.toString())
                    }else{
                        Toast.makeText(requireContext(),"Routine name can't be blank",Toast.LENGTH_SHORT).show()
                    }

                }

                setNegativeButton("Cancel"){dialog, which ->
                    Log.d("DialogEditText", "cancel button pressed")
                }

                setView(dialogLayout)
                show()
            }

        }

        editRoutineNameBtn.setOnClickListener {
            showEditTextDialog()
        }

//        viewModel.currentWorkouts.observe(viewLifecycleOwner) { exercises ->
//            exercisesAdapter.setData(exercises)
//        }
        ///

        val recyclerViewCurrent = view.findViewById<RecyclerView>(R.id.recyclerViewCurrentSession)
        recyclerViewCurrent.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewCurrent.adapter = exercisesAdapter



        viewModel.elapsedtime.observe(viewLifecycleOwner) { formattedTime ->
            currentTimer.text = formattedTime
        }

//        finishButton.setOnClickListener {
//            finishDialogBuilder.show()
//            viewModel.insertRoutineWithSets(viewModel.getRoutineObj(),viewModel.getAllCompletedSets())
//            Toast.makeText(requireContext(),"Routine inserted",Toast.LENGTH_SHORT).show()
//        }

        slideDownButton.setOnClickListener {
            navController?.navigate(R.id.sessionFragment)
        }


        buttonAdd.setOnClickListener {
            navController?.navigate(R.id.workoutPicker)
        }

        Log.d("currentSessionFragmentDebug", viewModel.workoutState.value.toString() + " Workout State")
        Log.d("currentSessionFragmentDebug", viewModel.templateEditState.value.toString() + " template edit state")
        Log.d("currentSessionFragmentDebug", viewModel.templateState.value.toString() + " template maker state")
}


    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("CurrentSession", "onDestroyView called")

    }

}