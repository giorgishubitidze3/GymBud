package com.example.fitnessapp.adapter

import android.app.Application
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.R
import com.example.fitnessapp.data.AppRepository
import com.example.fitnessapp.data.ExerciseInfo
import com.example.fitnessapp.data.GymExercise
import com.example.fitnessapp.data.Workout
import com.example.fitnessapp.data.WorkoutDao
import com.example.fitnessapp.data.WorkoutViewModel
import kotlinx.coroutines.launch

class CurrentSessionAdapter(viewModel: WorkoutViewModel, private val lifecycleOwner: LifecycleOwner): RecyclerView.Adapter<CurrentSessionAdapter.CurrentViewHolder>() {

    private var exercises: List<GymExercise> = emptyList()
    val viewModel = viewModel

    init {
            Log.d("observer2", "size of data: ${exercises.size}")
        Log.d("observer3", "data: ${exercises}")
    }



    inner class CurrentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvTitle = itemView.findViewById<TextView>(R.id.setTitle)
        val tvClass = itemView.findViewById<TextView>(R.id.tvClass2)
        val imageViewMuscle = itemView.findViewById<ImageView>(R.id.imageViewMuscle2)
        val addBtn = itemView.findViewById<Button>(R.id.addSetBtn)
        val setCounterLayout = itemView.findViewById<LinearLayout>(R.id.setCounterLayout)
        val editTextKG = itemView.findViewById<EditText>(R.id.etKG)
        val editTextREP = itemView.findViewById<EditText>(R.id.etREP)
        val checkBox = itemView.findViewById<CheckBox>(R.id.setCheckBox)
        val prev = itemView.findViewById<TextView>(R.id.previousSetTV)
        val removeBtn = itemView.findViewById<Button>(R.id.removeSetBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.current_session_rv_item, parent, false)
        return CurrentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    override fun onBindViewHolder(holder: CurrentViewHolder, position: Int) {
        val currentWorkout = exercises[position]

        holder.tvTitle?.text = currentWorkout.name ?: "Default Name"
        holder.prev?.text = currentWorkout.bodyPart ?: "Default Body Part"

        for(n in 1 until currentWorkout.setCount){
            val setCounterLayout = holder.itemView.findViewById<LinearLayout>(R.id.verticalLinear)
            addSetCounterLayout(setCounterLayout)
        }

        holder.addBtn.setOnClickListener {
            val setCounterLayout = holder.itemView.findViewById<LinearLayout>(R.id.verticalLinear)
            addSetCounterLayout(setCounterLayout)
            viewModel.addSetCount(currentWorkout)
        }

        holder.removeBtn.setOnClickListener {
            val setCounterLayout = holder.itemView.findViewById<LinearLayout>(R.id.verticalLinear)
            removeSetCounterLayout(setCounterLayout)
            viewModel.removeSetCount(currentWorkout)
        }

        holder.apply{
            val constraintLayout = itemView.findViewById<ConstraintLayout>(R.id.setsLayout)
            extractExerciseData(constraintLayout)
            Log.d("exerciseData",exerciseData.toString())
        }


    }



    data class SetData(val kg: String, val reps: String)
    data class SetItem(
//        val workoutName: String,
//        val setNumber: Int,
        val kg: String,
        val reps: String
    )

    val exerciseData = ArrayList<ExerciseInfo>()
    private fun extractExerciseData(constraintLayout: ConstraintLayout) {
        for(i in 0 until constraintLayout.childCount) {
            val dynamicLayout = constraintLayout.getChildAt(i) as ViewGroup
            val editTextWeight = dynamicLayout.findViewById<EditText>(R.id.etKG)
            val editTextRep = dynamicLayout.findViewById<EditText>(R.id.etREP)
            val workoutName = constraintLayout.findViewById<TextView>(R.id.setTitle).text.toString()
            val kgText = editTextWeight.text.toString()
            val repText = editTextRep.text.toString()
            val setNumber = 1
            val exerciseInfo = ExerciseInfo(
                setNumber,
                workoutName,
                kgText,
                repText
            )
            exerciseData.add(exerciseInfo)
        }
    }


    private fun addSetCounterLayout(linearLayout: LinearLayout) {
        val inflater = LayoutInflater.from(linearLayout.context)
        val setCounterLayout = inflater.inflate(R.layout.inner_rv_item, linearLayout, false)
        linearLayout.addView(setCounterLayout)
    }

    private fun removeSetCounterLayout(linearLayout: LinearLayout) {
        val childCount = linearLayout.childCount
        if (childCount > 1) {
            linearLayout.removeViewAt(childCount - 1)
        }
    }

    fun clearData(){
        exercises= emptyList()
        notifyDataSetChanged()
    }


    fun setData(newExercises: List<GymExercise>) {
        exercises = newExercises
        notifyDataSetChanged()
        Log.d("setData", "size of data: ${exercises.size}")
        Log.d("setData", "size of data: ${exercises}")
        Log.d("observer3", "data: ${exercises}")
    }

}


