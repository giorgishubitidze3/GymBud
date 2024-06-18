package com.example.fitnessapp.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.fitnessapp.R
import com.example.fitnessapp.adapter.MeasurementDetailAdapter
import com.example.fitnessapp.data.AggregatedMeasurement
import com.example.fitnessapp.data.Measurement
import com.example.fitnessapp.data.WorkoutViewModel
import com.example.fitnessapp.databinding.FragmentMeasurementsDetailBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
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

            setupChart(list)

        }

        binding.measurementDetailRecyclerView.adapter = adapter


    }

    private fun convertToEntries(measurements: List<Measurement>): List<Entry> {
        return measurements.map { measurement ->
            val dateInMillis = measurement.date
            Entry(dateInMillis.toFloat(), measurement.measurement.toFloat())
        }
    }

    private fun setupChart(measurements: List<Measurement>) {
        val aggregatedMeasurements = aggregateMeasurementsByDate(measurements)
        val entries = aggregatedMeasurements.mapIndexed { index, measurement ->
            Entry(index.toFloat(), measurement.sumMeasurement)
        }

        val dateLabels = aggregatedMeasurements.map { it.date }

        val dataSet = LineDataSet(entries, "Measurement Data").apply {
            color = Color.RED
            valueTextColor = Color.BLACK
        }

        val lineData = LineData(dataSet)
        val chart = binding.lineChart.apply {
            this?.data = lineData

            this?.xAxis?.apply {
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = DateValueFormatter(dateLabels)
                labelCount = dateLabels.size
                granularity = 1f
                setDrawLabels(true)
                setDrawGridLines(false)
                setAvoidFirstLastClipping(true)
            }

            this?.axisRight?.isEnabled = false
            this?.axisLeft?.apply {
                setDrawGridLines(false)
            }

            this?.description?.isEnabled = false
            this?.legend?.isEnabled = true
            this?.setTouchEnabled(true)
            this?.setPinchZoom(true)
            this?.invalidate()
        }
    }


    private fun getFormattedDate(value: Float): String {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val date = Date(value.toLong())
        return dateFormat.format(date)
    }

    private fun showAddMeasurementDialog(detail: String) {
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

        dateTextView.setOnClickListener { showDatePickerDialog(dateTextView) }

        dialogBuilder.setPositiveButton("Save") { dialog, _ ->
            val measurementValueText = measurementEditText.text.toString()
            val selectedDate = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).parse(dateTextView.text.toString())?.time

            if (measurementValueText.isNotEmpty() && selectedDate != null) {
                val measurementValue = measurementValueText.toDouble()
                workoutViewModel.insertMeasurementByDate(detail, measurementValue, selectedDate, workoutViewModel.currentUserId)

                workoutViewModel.getMeasurementByName(detail)
            } else if (measurementValueText.isNotEmpty()) {
                val measurementValue = measurementValueText.toDouble()
                workoutViewModel.insertMeasurement(detail, measurementValue)

                workoutViewModel.getMeasurementByName(detail)
            } else {
                Log.d("MeasurementDetailFragment", "Measurement value is invalid")
            }
        }

        dialogBuilder.setNegativeButton("Cancel") { dialog, _ -> }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }


    private fun showDatePickerDialog(dateTextView: TextView) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val selectedDate = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(calendar.time)
                dateTextView.text = selectedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    inner class DateValueFormatter(private val dates: List<Long>) : ValueFormatter() {
        private val dateFormat = SimpleDateFormat("MM/dd/yy", Locale.getDefault())

        override fun getFormattedValue(value: Float): String {
            val index = value.toInt()
            return if (index in dates.indices) {
                dateFormat.format(dates[index])
            } else {
                ""
            }
        }
    }


    private fun aggregateMeasurementsByDate(measurements: List<Measurement>): List<AggregatedMeasurement> {
        val grouped = measurements.groupBy { measurement ->
            val calendar = Calendar.getInstance().apply {
                timeInMillis = measurement.date
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            calendar.timeInMillis
        }

        return grouped.map { (date, measurementsOnDate) ->
            val sum = measurementsOnDate.map { it.measurement }.sum().toFloat()
            AggregatedMeasurement(date, sum)
        }.sortedBy { it.date }
    }

}