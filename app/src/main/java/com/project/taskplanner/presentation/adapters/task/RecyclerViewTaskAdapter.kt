package com.project.taskplanner.presentation.adapters.task

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.graphics.Paint;
import android.graphics.drawable.Icon
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.project.domain.models.TaskInterim
import com.project.taskplanner.R
import com.project.taskplanner.databinding.ItemTaskRecyclerviewBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class RecyclerViewTaskAdapter(val context : Context) : RecyclerView.Adapter<RecyclerViewTaskAdapter.MyViewHolder>() {

    public var taskList = ArrayList<TaskInterim>()
    private var listener : OnItemClickListener? = null
    inner class MyViewHolder(itemView : View) : ViewHolder(itemView){

        private val binding = ItemTaskRecyclerviewBinding.bind(itemView)

        fun bind(taskInterim: TaskInterim) = with(binding){
            textViewTaskTitle.text = taskInterim.title
            textviewDescTaskPreview.text= taskInterim.description
            cardViewColorTask.setCardBackgroundColor(taskInterim.color)

            setStates(
                taskInterim.isChecked,
                taskInterim.appointedDate,
                taskInterim.completionDate)

            itemView.setOnClickListener {
                listener?.let {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        listener!!.onItemClick(itemView, position)
                    }
                }
            }

            itemView.setOnLongClickListener {

                listener?.let {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        listener!!.onItemLongClick(itemView, position)
                    }
                }

                return@setOnLongClickListener true
            }
        }

        private fun setStates(
            isChecked : Boolean,
            appointedDate: LocalDate,
            completionDate : LocalDate?) = with(binding)
        {
            when(isChecked){
                true -> {
                    textViewTaskTitle.apply {
                        paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    }

                    textviewDescTaskPreview.apply {
                        paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    }

                    imageViewTask.apply {
                        setImageIcon(Icon.createWithResource(context, R.drawable.ic_check))
                        setColorFilter(Color.BLUE)
                    }

                    textViewDateTaskPreview.apply {
                        val formattedString = resources.getString(
                            R.string.completed_task_string,
                            completionDate
                                ?.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                        )
                        text = formattedString
                        setTextColor(Color.BLUE)
                    }
                }


                false -> {
                    textViewTaskTitle.apply {
                        paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    }

                    textviewDescTaskPreview.apply {
                        paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG
                    }

                    imageViewTask.apply {
                        setImageIcon(Icon.createWithResource(context, R.drawable.ic_calendar))
                        setColorFilter(Color.BLACK)
                    }

                    textViewDateTaskPreview.apply {
                        text = appointedDate
                            .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
                    }
                }
                else -> {}
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task_recyclerview, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return taskList.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(taskList[position])
    }

    fun addTask(taskInterim: TaskInterim){
        taskList.add(taskInterim)
        notifyItemInserted(taskList.lastIndex)
    }

    fun deleteTask(position : Int){
        taskList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemId(position: Int): Long {
        return taskList[position].id.toLong()
    }

    interface OnItemClickListener{
        fun onItemClick(itemView : View, position : Int)
        fun onItemLongClick(itemView : View, position : Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

}