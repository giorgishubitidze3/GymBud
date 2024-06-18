package com.example.fitnessapp.adapter

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.R
import com.example.fitnessapp.data.Measurement
import com.example.fitnessapp.data.WorkoutViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MeasurementDetailAdapter(val context: Context, val workoutViewModel: WorkoutViewModel): RecyclerView.Adapter<MeasurementDetailAdapter.ViewHolder>() {

    var list : List<Measurement> = emptyList()

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val date = itemView.findViewById<TextView>(R.id.item_measurement_detail_date)
        val measurement = itemView.findViewById<TextView>(R.id.item_measurement_detail_measurement)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_measurement_detail, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]

        holder.measurement.text = currentItem.measurement.toString()
        holder.date.text= formatDate(currentItem.date)

        holder.itemView.setOnClickListener {
            showEditMeasurementDialog(currentItem, currentItem.name )
        }

    }

    fun setData(list: List<Measurement>){
        this.list = list
        notifyDataSetChanged()
    }

    private fun formatDate(timestamp: Long): String {
        val date = Date(timestamp)
        val format = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return format.format(date)
    }


    private fun showEditMeasurementDialog(measurement: Measurement, detail: String) {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogLayout = inflater.inflate(R.layout.edit_text_dialog_measurement_edit, null)

        val titleTextView = dialogLayout.findViewById<TextView>(R.id.edit_text_layout_measurement_title_edit)
        val dateTextView = dialogLayout.findViewById<TextView>(R.id.edit_text_layout_measurement_date_edit)
        val measurementEditText = dialogLayout.findViewById<EditText>(R.id.et_dialogEditText_edit)
        val deleteButton = dialogLayout.findViewById<Button>(R.id.button_delete_edits)
        val cancelButton = dialogLayout.findViewById<Button>(R.id.button_cancel_edit)
        val updateButton = dialogLayout.findViewById<Button>(R.id.button_third_edit)

        titleTextView.text = detail
        dateTextView.text = formatDate(measurement.date)
        measurementEditText.setText(measurement.measurement.toString())


        val dialog = builder.setView(dialogLayout).create()

        updateButton.setOnClickListener {
            val measurementValue = measurementEditText.text.toString().toDoubleOrNull()

            if (measurementValue != null) {
                workoutViewModel.updateMeasurementById(measurement.measurementId, workoutViewModel.currentUserId, measurementValue)
                workoutViewModel.getMeasurementByName(detail)
                dialog.dismiss()
            } else {
                Log.d("MeasurementDetailFragment", "Measurement edit text value null or empty")
            }
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        deleteButton.setOnClickListener {
            workoutViewModel.deleteMeasurementById(measurement.measurementId, workoutViewModel.currentUserId)
            workoutViewModel.getMeasurementByName(detail)
            dialog.dismiss()
        }

        dialog.show()
    }



    }