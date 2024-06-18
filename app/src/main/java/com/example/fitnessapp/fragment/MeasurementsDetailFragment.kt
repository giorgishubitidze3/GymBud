package com.example.fitnessapp.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import com.example.fitnessapp.R
import com.example.fitnessapp.adapter.MeasurementDetailAdapter
import com.example.fitnessapp.data.GymExercise
import com.example.fitnessapp.data.WorkoutViewModel
import com.example.fitnessapp.databinding.FragmentMeasurementsDetailBinding
import kotlinx.coroutines.selects.select
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MeasurementsDetailFragment : Fragment() {

    private lateinit var binding: FragmentMeasurementsDetailBinding
    private lateinit var workoutViewModel:WorkoutViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMeasurementsDetailBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         workoutViewModel = ViewModelProvider(requireActivity())[WorkoutViewModel::class.java]

        val selectedMeasurement = arguments?.getString("selectedMeasurement")
        Log.d("WorkoutDetails", "Selected exercise in WorkoutDetails: $selectedMeasurement")

        binding.measurementsDetailTitle.text = selectedMeasurement

        if (selectedMeasurement != null) {
            workoutViewModel.getMeasurementByName(selectedMeasurement)
        }

        binding.imageButton.setOnClickListener {
            if (selectedMeasurement != null) {
                showAddMeasurementDialog(selectedMeasurement)
            }
        }

        val adapter = MeasurementDetailAdapter(requireContext(),workoutViewModel)

        workoutViewModel.currentMeasurementDetailsList.observe(viewLifecycleOwner){list ->
            adapter.setData(list)
        }

        binding.measurementDetailRecyclerView.adapter = adapter


    }

    private fun showAddMeasurementDialog(detail: String){
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.edit_text_dialog_measurement, null)
        dialogBuilder.setView(dialogView)

        val titleTextView = dialogView.findViewById<TextView>(R.id.edit_text_layout_measurement_title)
        val dateTextView = dialogView.findViewById<TextView>(R.id.edit_text_layout_measurement_date)
        val measurementEditText = dialogView.findViewById<EditText>(R.id.et_dialogEditText)

        titleTextView.text = detail

        val currentDate = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(Date())
        dateTextView.text = currentDate

        dialogBuilder.setPositiveButton("Save") { dialog, _ ->
            val measurementValueText = measurementEditText.text.toString()

            if(measurementValueText.isNotEmpty()){
                val measurementValue = measurementValueText.toDouble()
                workoutViewModel.insertMeasurement(detail,measurementValue)

                workoutViewModel.getMeasurementByName(detail)

            }else{
                Log.d("MeasurementDetailFragment","measurement edit text value null or empty")
            }

        }

        dialogBuilder.setNegativeButton("Cancel") { dialog, _ ->

        }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }


}