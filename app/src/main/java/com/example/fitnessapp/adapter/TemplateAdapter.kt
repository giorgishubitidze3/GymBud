package com.example.fitnessapp.adapter

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.R
import com.example.fitnessapp.data.TemplateWithSets
import com.example.fitnessapp.data.WorkoutViewModel

class TemplateAdapter(val context:Context , val viewModel : WorkoutViewModel, private val navController : NavController, val viewLifecycleOwner: LifecycleOwner): RecyclerView.Adapter<TemplateAdapter.ViewHolder>() {

    var list : List<TemplateWithSets> = listOf()
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val templateName = itemView.findViewById<TextView>(R.id.template_name)
        val templateEditButton = itemView.findViewById<ImageButton>(R.id.template_edit_btn)
        val childRecyclerView = itemView.findViewById<RecyclerView>(R.id.template_rv)

//        init {
//            templateEditButton.setOnClickListener { v ->
//                showPopupMenu(v)
//            }
//        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.template_rv_item, parent , false)
        return ViewHolder(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]

        holder.templateName.text = currentItem.template.name

        val childLayoutManager = LinearLayoutManager(
            holder.childRecyclerView.context,
            LinearLayoutManager.VERTICAL,
            false
        )

        holder.childRecyclerView.layoutManager = childLayoutManager

        val adapter = InnerTemplateAdapter(currentItem.templateSets)

        holder.childRecyclerView.adapter = adapter


        holder.templateEditButton.setOnClickListener {
            showPopupMenu(it, currentItem)
        }


        holder.itemView.setOnClickListener{
            openWorkoutDialog(currentItem)
        }

    }

    fun setData(list: List<TemplateWithSets>){
        this.list = list
        notifyDataSetChanged()
    }
    private fun showPopupMenu(view: View, currentItem: TemplateWithSets) {
        val popup = PopupMenu(view.context, view)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.menu_template_popup, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.template_popup_edit -> {
                    openEditDialog(currentItem)
                    true
                }
                R.id.template_popup_rename -> {
                    openRenameDialog(currentItem)
                    true
                }
                R.id.template_popup_delete -> {
                    openDeleteDialog(currentItem)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }



    private fun openRenameDialog(item : TemplateWithSets){
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogLayout = inflater.inflate(R.layout.edit_text_dialog_layout, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.et_dialogEditText)

        with(builder){
            setTitle("Enter template name")
            setPositiveButton("OK"){dialog, which ->
                if(editText.text.toString().isNotBlank()){
                    viewModel.updateTemplateName(item.template.templateId,editText.text.toString())
                }else{
                    Log.d("template viewModel" ,"pressed no on edit btn")
                }
            }

            setNegativeButton("NO"){dialog, which ->
                Log.d("template viewModel" , "pressed no on the dialog")
            }

            setView(dialogLayout)
            show()
        }
    }


    private fun openDeleteDialog(item: TemplateWithSets){
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)

        with(builder){
            setTitle("Do you want to delete the template")
            setPositiveButton("YES"){_, _ ->
                viewModel.deleteTemplateById(item.template.templateId)
            }

            setNegativeButton("NO"){_,_ ->
                Log.d("templateViewModel", "pressed no on delete dialog")
            }

            show()
        }
    }


    private fun openEditDialog(item: TemplateWithSets){
        val builder = AlertDialog.Builder(context)

        with(builder){
            setTitle("Do you want to edit this template?")
            setPositiveButton("YES"){_,_ ->
                viewModel.endWorkout()
                viewModel.stopTimer()
                viewModel.resetCurrentSets()
                viewModel.resetCurrentWorkouts()
                viewModel.startTemplateEditor()
                viewModel.loadTemplateIntoCurrent(item)
                viewModel.changeRoutineName(item.template.name)
                navController.navigate(R.id.currentSession)
            }
            setNegativeButton("NO"){_,_ ->
                Log.d("templateViewModel", "pressed no on edit dialog")
            }

            show()
        }
    }


    private fun openWorkoutDialog(item: TemplateWithSets){
        val builder = AlertDialog.Builder(context)

        with(builder){
            viewModel.workoutState.observe(viewLifecycleOwner){ workoutProgress ->
                if(workoutProgress){
                    setTitle("Workout in progress")
                    setMessage("Do you want to start a new workout?")
                    setPositiveButton("YES"){_,_ ->
                        viewModel.resetCurrentWorkouts()
                        viewModel.resetCurrentSets()
                        viewModel.endWorkout()
                        viewModel.stopTimer()
                        viewModel.endTemplateEditor()
                        viewModel.endTemplateMaker()

                        viewModel.loadTemplateIntoCurrent(item)
                        viewModel.startWorkout()
                        viewModel.startTimer()
                        viewModel.changeRoutineName(item.template.name)

                        navController.navigate(R.id.currentSession)

                    }

                    setNegativeButton("NO"){_,_ ->

                    }


            }
                else{
                    setTitle("Do you want to start a workout?")
                    setPositiveButton("YES"){_,_ ->
                        viewModel.resetCurrentWorkouts()
                        viewModel.resetCurrentSets()
                        viewModel.endTemplateEditor()
                        viewModel.endTemplateMaker()


                        viewModel.loadTemplateIntoCurrent(item)
                        viewModel.startWorkout()
                        viewModel.startTimer()
                        viewModel.changeRoutineName(item.template.name)

                        navController.navigate(R.id.currentSession)
                    }
                    setNegativeButton("NO"){_,_ ->

                    }
                }

        }

            show()
    }

}
}