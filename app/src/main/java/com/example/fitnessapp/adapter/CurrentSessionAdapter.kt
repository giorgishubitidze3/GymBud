package com.example.fitnessapp.adapter

import InnerSetAdapter
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
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

class CurrentSessionAdapter(
    private val viewModel: WorkoutViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val context: Context
) : RecyclerView.Adapter<CurrentSessionAdapter.CurrentViewHolder>() {

    private var exercises: List<GymExercise> = emptyList()

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
            val newSetId = viewModel.increaseSetId(currentWorkout.name)
            val newSet = WorkoutSet(currentWorkout.name, newSetId, 0, 0, 0, false)
            viewModel.addSet(newSet)
        }

        holder.removeBtn.setOnClickListener {
            val currentSets = viewModel.currentSets.value?.filter { it.workoutName == currentWorkout.name } ?: return@setOnClickListener
            if (currentSets.size > 1) {
                val setToRemove = currentSets.last()
                viewModel.removeSet(setToRemove)
                viewModel.decreaseSetId(currentWorkout.name)
            } else {
                Toast.makeText(context, "Cannot remove the last set", Toast.LENGTH_SHORT).show()
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
}

