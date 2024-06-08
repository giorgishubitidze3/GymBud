package com.example.fitnessapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.R
import com.example.fitnessapp.data.TemplateWithSets

class TemplateAdapter(): RecyclerView.Adapter<TemplateAdapter.ViewHolder>() {

    var list : List<TemplateWithSets> = listOf()
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val templateName = itemView.findViewById<TextView>(R.id.template_name)
        val templateEditButton = itemView.findViewById<ImageButton>(R.id.template_edit_btn)
        val childRecyclerView = itemView.findViewById<RecyclerView>(R.id.template_rv)
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

    }

    fun setData(list: List<TemplateWithSets>){
        this.list = list
        notifyDataSetChanged()
    }
}