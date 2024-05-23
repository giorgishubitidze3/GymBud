package com.example.fitnessapp.adapter

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.fitnessapp.MyApplication
import com.example.fitnessapp.R
import com.example.fitnessapp.data.AppDatabase
import com.example.fitnessapp.data.GymExercise
import com.example.fitnessapp.data.WorkoutViewModel

class AddExerciseAdapter
    (val context: Context,  var exercises: List<GymExercise>, private val myApplication: MyApplication,
     val viewModel: WorkoutViewModel,
     private val switchToDetailCallback: (GymExercise) -> Unit): RecyclerView.Adapter<AddExerciseAdapter.ExerciseViewHolder>() {

    val appDatabase: AppDatabase = myApplication.database

    var exercisesAll : MutableList<GymExercise> = exercises.toMutableList()

    fun setData(newData: List<GymExercise>) {
        exercises = newData
        notifyDataSetChanged()
    }

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle2)
        val tvClass = itemView.findViewById<TextView>(R.id.tvClass2)
        val imageViewMuscle = itemView.findViewById<ImageView>(R.id.imageViewMuscle2)
        val addBtn = itemView.findViewById<ImageButton>(R.id.add_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_add_exercise_item, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.tvTitle.setTextIsSelectable(true)
        holder.tvTitle.isSelected
        holder.tvTitle.text = exercise.name
        holder.tvClass.text = exercise.bodyPart

        val gifUrl = exercise.gifUrl
        val resourceId = context.resources.getIdentifier(gifUrl, "drawable", context.packageName)
        holder.imageViewMuscle.setImageResource(resourceId)

        holder.itemView.setOnClickListener {
            switchToDetailCallback(exercise)
        }

        holder.addBtn.setOnClickListener {
            val currentWorkouts = viewModel.currentWorkouts.value ?: emptyList()
            if (currentWorkouts.any { it.name == exercise.name }) {
                Toast.makeText(context, "Exercise already added", Toast.LENGTH_SHORT).show()
                vibratePhone(context, 500)
            } else {
                viewModel.updateCurrentWorkout(exercise)
                vibratePhone(context,50)
                Log.d("addExerciseBtn", "exercise btn pressed")
            }
        }
    }

    private fun vibratePhone(context: Context, duration: Long) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(100)
        }
    }
//    override fun getFilter(): Filter {
//        return object : Filter() {
//            //background thread
//            override fun performFiltering(charSequence: CharSequence?): FilterResults {
//                val filteredList = mutableListOf<GymExercise>()
//
//                if (charSequence == null || charSequence.isEmpty()) {
//                    filteredList.addAll(exercisesAll)
//                } else {
//                    val filterPattern = charSequence.toString().toLowerCase().trim()
//                    for (item in exercisesAll) {
//                        if (item.name.toLowerCase().contains(filterPattern)) {
//                            filteredList.add(item)
//                        }
//                    }
//                }
//
//                val results = FilterResults()
//                results.values = filteredList
//                return results
//            }
//
//            // Automatic on UI thread
//            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults?) {
//                exercisesAll.clear()
//                exercisesAll.addAll(filterResults?.values as Collection<GymExercise>)
//                notifyDataSetChanged()
//            }
//        }
//    }

}



