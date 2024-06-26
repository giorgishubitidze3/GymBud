package com.example.fitnessapp.adapter

import InnerSetAdapter
import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.R
import com.example.fitnessapp.data.GymExercise
import com.example.fitnessapp.data.WorkoutSet
import com.example.fitnessapp.data.WorkoutViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class CurrentSessionAdapter(
    private val viewModel: WorkoutViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val context: Context
) : RecyclerView.Adapter<CurrentSessionAdapter.CurrentViewHolder>() {

    private var exercises: List<GymExercise> = emptyList()
    val auth = Firebase.auth

    inner class CurrentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView? = itemView.findViewById(R.id.setTitle)
        val tvClass: TextView? = itemView.findViewById(R.id.tvClass2)
        val addBtn: Button = itemView.findViewById(R.id.addSetBtn)
        val removeBtn: Button = itemView.findViewById(R.id.removeSetBtn)
        val childRecyclerView: RecyclerView = itemView.findViewById(R.id.childRecyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.current_session_rv_item, parent, false)
        return CurrentViewHolder(view)
    }

    override fun getItemCount(): Int = exercises.size

    override fun onBindViewHolder(holder: CurrentViewHolder, position: Int) {
        val currentWorkout = exercises[position]

        holder.tvTitle?.text = currentWorkout.name ?: "Default Name"
        holder.tvClass?.text = currentWorkout.bodyPart ?: "Default Body Part"

        val childLayoutManager = LinearLayoutManager(
            holder.childRecyclerView.context,
            LinearLayoutManager.VERTICAL,
            false
        )
        holder.childRecyclerView.layoutManager = childLayoutManager

        val innerAdapter = InnerSetAdapter(emptyList(), viewModel, lifecycleOwner)
        holder.childRecyclerView.adapter = innerAdapter

        viewModel.currentSets.observe(lifecycleOwner) { sets ->
            val filteredSets = sets.filter { it.workoutName == currentWorkout.name }
            innerAdapter.setData(filteredSets)
        }

        holder.addBtn.setOnClickListener {
            val newSetId = viewModel.generateUniqueSetId(currentWorkout.name)
            val newSet = WorkoutSet(0,newSetId,0,currentWorkout.name, 0, 0, 0, false, auth.currentUser?.uid.toString())

            viewModel.addSet(newSet)
            innerAdapter.setData(viewModel.currentSets.value?.filter { it.workoutName == currentWorkout.name } ?: emptyList())
        }

        holder.removeBtn.setOnClickListener {
            val currentSets = viewModel.currentSets.value?.filter { it.workoutName == currentWorkout.name } ?: return@setOnClickListener
            if (currentSets.size > 1) {
                val setToRemove = currentSets.last()
                viewModel.removeSet(setToRemove)
            } else {
                val lastSet = currentSets.last()
                openDeleteWorkoutDialog(currentWorkout, lastSet)
                vibratePhone(context)
            }
        }
    }

    fun setData(newExercises: List<GymExercise>) {
        exercises = newExercises
        notifyDataSetChanged()
    }

    private fun vibratePhone(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(100)
        }
    }

    private fun openDeleteWorkoutDialog(currentExercise: GymExercise, set: WorkoutSet){
        val builder = AlertDialog.Builder(context)

        with(builder){
            setTitle("Exercise will be deleted")
            setMessage("Do you want to delete the exercise?")
            setPositiveButton("YES"){_,_ ->
                viewModel.removeWorkout(currentExercise)
                viewModel.removeSet(set)
            }
            setNegativeButton("NO"){_,_ ->
                Log.d("CurrentSessionAdapter" , "pressed no on the delete workout dialog")
            }

            show()
        }
    }
}

