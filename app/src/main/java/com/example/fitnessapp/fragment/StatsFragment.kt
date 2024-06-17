package com.example.fitnessapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.fitnessapp.R
import com.example.fitnessapp.data.WorkoutViewModel
import com.example.fitnessapp.databinding.FragmentStatsBinding
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.delay
import java.util.Calendar


class StatsFragment : Fragment() {

    private lateinit var binding: FragmentStatsBinding
    private lateinit var workoutViewModel: WorkoutViewModel
    private var activeButton: View? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workoutViewModel = ViewModelProvider(requireActivity())[WorkoutViewModel::class.java]


        Log.d("StatsFragment", "setting active button")
        setActiveButton(binding.buttonWeek)
        updateDataForWeek()
        observeWeekData()

        binding.buttonWeek.setOnClickListener {
            setActiveButton(binding.buttonWeek)
            updateDataForWeek()
            observeWeekData()
        }
        binding.buttonMonth.setOnClickListener {
            setActiveButton(binding.buttonMonth)
            updateDataForMonth()
            observeMonthData()
        }
        binding.buttonYear.setOnClickListener {
            setActiveButton(binding.buttonYear)
            updateDataForYear()
            observeYearData()
        }

        binding.buttonLifetime.setOnClickListener {
            setActiveButton(binding.buttonLifetime)
            updateDataForLifetime()
            observeLifetimeData()
        }

        binding.buttonCustomDate.setOnClickListener {
            showDateRangePickerDialog()
        }
    }

//    private fun setActiveButton(activeButton: Button) {
//        val buttons = listOf<Button>(
//            binding.buttonWeek,
//            binding.buttonMonth,
//            binding.buttonYear,
//            binding.buttonLifetime
//        )
//
//        buttons.forEach { button ->
//            if (button == activeButton) {
//                button.setBackgroundResource(R.drawable.shape_rounded_stats_card)
//                button.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
//            } else {
//                button.setBackgroundResource(android.R.color.transparent)
//                button.setTextColor(ContextCompat.getColor(requireContext(), R.color.blueblack))
//            }
//        }
//    }

    private fun setActiveButton(button: View) {
        activeButton?.let {
            it.setBackgroundResource(android.R.color.transparent)
            if (it is Button) {
                it.setTextColor(ContextCompat.getColor(requireContext(), R.color.blueblack))
            }
        }

        // Set the new active button
        activeButton = button
        button.setBackgroundResource(R.drawable.shape_rounded_stats_card)

        if (button is Button) {
            button.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }else if (button is ImageButton) {
            button.setColorFilter(ContextCompat.getColor(requireContext(), R.color.blueblack))
        }
    }



    private fun observeWeekData() {

        workoutViewModel.currentRoutinesAndSetsForWeek.observe(viewLifecycleOwner) { (routines,sets) ->
            binding.totalSessionsTv.text = routines.size.toString()

            if(sets.isNullOrEmpty()){
                binding.totalRepsTv.text = "No data available"
                binding.totalWeightTv.text = "No data available"
            }else{
                var totalWeight = 0;
                var totalReps = 0;
                sets.forEach { item ->
                    Log.d("StatsFragment","$item this is set from currentWorkoutSetsForWeek")
                    totalWeight += item.currentKg
                    totalReps += item.currentReps
                }
                binding.totalRepsTv.text = totalReps.toString()
                binding.totalWeightTv.text = totalWeight.toString()
            }
        }
    }


    private fun observeMonthData() {
        workoutViewModel.currentRoutinesAndSetsForMonth.observe(viewLifecycleOwner) { (routines,sets) ->
            binding.totalSessionsTv.text = routines.size.toString()

            if(sets.isNullOrEmpty()){
                binding.totalRepsTv.text = "No data available"
                binding.totalWeightTv.text = "No data available"
            }else{
                var totalWeight = 0;
                var totalReps = 0;
                sets.forEach { item ->
                    Log.d("StatsFragment","$item this is set from currentWorkoutSetsForWeek")
                    totalWeight += item.currentKg
                    totalReps += item.currentReps
                }
                binding.totalRepsTv.text = totalReps.toString()
                binding.totalWeightTv.text = totalWeight.toString()
            }
        }
    }

    private fun observeYearData() {
        workoutViewModel.currentRoutinesAndSetsForYear.observe(viewLifecycleOwner) { (routines,sets) ->
            binding.totalSessionsTv.text = routines.size.toString()

            if(sets.isNullOrEmpty()){
                binding.totalRepsTv.text = "No data available"
                binding.totalWeightTv.text = "No data available"
            }else{
                var totalWeight = 0;
                var totalReps = 0;
                sets.forEach { item ->
                    Log.d("StatsFragment","$item this is set from currentWorkoutSetsForWeek")
                    totalWeight += item.currentKg
                    totalReps += item.currentReps
                }
                binding.totalRepsTv.text = totalReps.toString()
                binding.totalWeightTv.text = totalWeight.toString()
            }
        }
    }

    private fun observeLifetimeData(){
        workoutViewModel.currentRoutinesAndSetsForLifetime.observe(viewLifecycleOwner) { (routines, sets) ->
            binding.totalSessionsTv.text = routines.size.toString()

            if (sets.isEmpty()) {
                binding.totalRepsTv.text = "No data available"
                binding.totalWeightTv.text = "No data available"
            } else {
                var totalWeight = 0;
                var totalReps = 0;
                sets.forEach { item ->
                    Log.d("StatsFragment","$item this is set from currentWorkoutSetsForWeek")
                    totalWeight += item.currentKg
                    totalReps += item.currentReps
                }
                binding.totalRepsTv.text = totalReps.toString()
                binding.totalWeightTv.text = totalWeight.toString()
            }
        }
    }

    private fun observeCustomData(){
        workoutViewModel.currentRoutinesAndSetsForCustomDate.observe(viewLifecycleOwner) { (routines, sets) ->
            binding.totalSessionsTv.text = routines.size.toString()

            if (sets.isEmpty()) {
                binding.totalRepsTv.text = "No data available"
                binding.totalWeightTv.text = "No data available"
            } else {
                var totalWeight = 0;
                var totalReps = 0;
                sets.forEach { item ->
                    Log.d("StatsFragment","$item this is set from currentWorkoutSetsForWeek")
                    totalWeight += item.currentKg
                    totalReps += item.currentReps
                }
                binding.totalRepsTv.text = totalReps.toString()
                binding.totalWeightTv.text = totalWeight.toString()
            }
        }
    }

    private fun updateDataForWeek() {
        val weekAgo = getDateOneWeekAgo()
        workoutViewModel.getRoutinesAndSetsForPeriod(weekAgo, System.currentTimeMillis(), "Week")
    }

    private fun updateDataForMonth() {
        val monthAgo = getDateOneMonthAgo()
        workoutViewModel.getRoutinesAndSetsForPeriod(monthAgo, System.currentTimeMillis(), "Month")
    }

    private fun updateDataForYear() {
        val yearAgo = getDateOneYearAgo()
        workoutViewModel.getRoutinesAndSetsForPeriod(yearAgo, System.currentTimeMillis(), "Year")
    }

    private fun updateDataForLifetime(){
        workoutViewModel.getRoutinesAndSetsLifetime()
    }


    private fun getDateOneWeekAgo(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.add(Calendar.WEEK_OF_YEAR, -1)
        return calendar.timeInMillis
    }

    private fun getDateOneMonthAgo(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.add(Calendar.MONTH, -1)
        return calendar.timeInMillis
    }

    private fun getDateOneYearAgo(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.add(Calendar.YEAR, -1)
        return calendar.timeInMillis
    }

    private fun showDateRangePickerDialog() {
        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select dates")
            .build()

        dateRangePicker.show(childFragmentManager, "date_range_picker")

        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            val startDate = selection.first ?: return@addOnPositiveButtonClickListener
            val endDate = selection.second ?: return@addOnPositiveButtonClickListener
            workoutViewModel.getRoutinesAndSetsForPeriod(startDate, endDate, "CustomDate")
            setActiveButton(binding.buttonCustomDate)
            observeCustomData()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("StatsFragment", "stats fragment onDestroy() call")
    }

    override fun onPause() {
        super.onPause()
        Log.d("StatsFragment", "stats fragment onPause() call")
    }
}