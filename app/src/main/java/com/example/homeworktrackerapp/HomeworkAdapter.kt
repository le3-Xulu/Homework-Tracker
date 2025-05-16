package com.example.homeworktrackerapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HomeworkAdapter(private val homeworkList: List<Homework>) :
    RecyclerView.Adapter<HomeworkAdapter.HomeworkViewHolder>() {

    class HomeworkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subject: TextView = itemView.findViewById(R.id.tvSubject)
        val description: TextView = itemView.findViewById(R.id.tvDescription)
        val dueDate: TextView = itemView.findViewById(R.id.tvDueDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeworkViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_homework, parent, false)
        return HomeworkViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeworkViewHolder, position: Int) {
        val item = homeworkList[position]
        holder.subject.text = item.subject
        holder.description.text = item.description
        holder.dueDate.text = "Due: ${item.dueDate}"
    }

    override fun getItemCount(): Int = homeworkList.size
}
