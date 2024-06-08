package com.example.fitnessapp.adapter

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.R
import com.example.fitnessapp.data.TemplateWithSets
import com.example.fitnessapp.data.WorkoutViewModel

class TemplateAdapter(val context:Context , val viewModel : WorkoutViewModel): RecyclerView.Adapter<TemplateAdapter.ViewHolder>() {

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
                    Toast.makeText(view.context, "Item 1", Toast.LENGTH_SHORT).show()
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

}