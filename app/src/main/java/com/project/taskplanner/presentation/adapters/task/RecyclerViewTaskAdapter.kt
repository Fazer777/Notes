package com.project.taskplanner.presentation.adapters.task

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.graphics.Paint;
import android.graphics.drawable.Icon
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.project.domain.models.TaskInterim
import com.project.taskplanner.R
import com.project.taskplanner.databinding.ItemTaskRecyclerviewBinding
import com.project.taskplanner.presentation.fragments.tasks.LayoutEnum
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class RecyclerViewTaskAdapter() : RecyclerView.Adapter<RecyclerViewTaskAdapter.MyViewHolder>() {

    private var taskList = ArrayList<TaskInterim>()
    private var listener : OnItemClickListener? = null
    private var layoutEnum : LayoutEnum = LayoutEnum.LINEAR
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
                listener?.let {listener->
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        listener.onItemClick(itemView, position)
                    }
                }
            }

            itemView.setOnLongClickListener {

                listener?.let {listener->
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        listener.onItemLongClick(itemView, position)
                    }
                }

                return@setOnLongClickListener true
            }

            imbPopUp.setOnClickListener {
                listener?.let {listener->
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        listener.onThreeDotClick(itemView, position)
                    }
                }
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

                    customTextViewDatePreview(completionDate)
                }


                false -> {
                    textViewTaskTitle.apply {
                        paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    }

                    textviewDescTaskPreview.apply {
                        paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    }

                    imageViewTask.apply {
                        setImageIcon(Icon.createWithResource(context, R.drawable.ic_calendar))
                        setColorFilter(Color.BLACK)
                    }

                    textViewDateTaskPreview.apply {
                        text = appointedDate
                            .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
                        setTextColor(context.resources.getColor(R.color.grey_default, context.theme))
                    }
                }
            }
        }

        private fun customTextViewDatePreview(completionDate: LocalDate?) = with(binding){
            val dateStr = completionDate?.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
            textViewDateTaskPreview.apply {

                text = when(layoutEnum){
                    LayoutEnum.LINEAR -> { resources.getString(R.string.completed_task_string_full, dateStr) }
                    LayoutEnum.GRID -> { resources.getString(R.string.completed_task_string_short, dateStr) }
                }

                textSize = when(layoutEnum){
                    LayoutEnum.LINEAR ->{ 15f }
                    LayoutEnum.GRID ->  { 11.5f }
                }

                setTextColor(Color.BLUE)
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

    fun updateTask(position: Int, taskInterim: TaskInterim){
        taskList[position] = taskInterim
        notifyItemChanged(position)
    }

    fun setAdapterList(newList : List<TaskInterim>){
        taskList.clear()
        taskList.addAll(newList)
        notifyDataSetChanged()
    }


    override fun getItemId(position: Int): Long {
        return taskList[position].id.toLong()
    }

    fun getItem(position: Int) : TaskInterim{
        return taskList[position]
    }

    fun updateTaskFlag(position: Int, flag : Boolean, completionDate: LocalDate){
        taskList[position].isChecked = flag
        taskList[position].completionDate = completionDate
        notifyItemChanged(position)
    }

    fun updateLayout(layout : LayoutEnum){
        layoutEnum = layout
        notifyDataSetChanged()
    }

    interface OnItemClickListener{
        fun onItemClick(itemView : View, position : Int)
        fun onItemLongClick(itemView : View, position : Int)
        fun onThreeDotClick(itemView : View, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

}