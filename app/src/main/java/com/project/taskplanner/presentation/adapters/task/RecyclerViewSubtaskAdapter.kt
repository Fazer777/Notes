package com.project.taskplanner.presentation.adapters.task

import android.content.Context
import android.graphics.drawable.Icon
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.project.domain.models.task.SubtaskParam
import com.project.taskplanner.R
import com.project.taskplanner.databinding.ItemSubtaskRecyclerviewBinding


class RecyclerViewSubtaskAdapter(val context : Context) : RecyclerView.Adapter<RecyclerViewSubtaskAdapter.MyViewHolder>() {

    private var subTaskList = ArrayList<SubtaskParam>()
    private var listener : OnItemViewClickListener? = null
    inner class MyViewHolder(itemView : View) : ViewHolder(itemView){
        private val binding = ItemSubtaskRecyclerviewBinding.bind(itemView)
        fun bind(subtaskParam: SubtaskParam) = with(binding){

            setCheckedSubtask(subtaskParam.isChecked)
            subItemTextViewName.text = subtaskParam.title

            imageBtnChecking.setOnClickListener {view ->
                listener?.let {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        listener?.onCheckboxItemClick(
                            view,
                            position,
                            subtaskParam.isChecked
                        )
                    }
                }
            }

            imageBtnDeleteSubtask.setOnClickListener {view ->
                listener?.let {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        listener?.onDeleteButtonClick(view, position)
                    }
                }
            }
        }

        private fun setCheckedSubtask(isChecked : Boolean) = with(binding){
            when(isChecked){
                true ->{
                    imageBtnChecking.setImageIcon(
                        Icon.createWithResource(context, R.drawable.ic_circle_checked)
                    )
                }

                false ->{
                    imageBtnChecking.setImageIcon(
                        Icon.createWithResource(context, R.drawable.ic_circle_unchecked)
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_subtask_recyclerview, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return subTaskList.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(subTaskList[position])
    }

     fun getItem(position: Int) : SubtaskParam {
        return subTaskList[position]
    }

    fun addSubTask(subtaskParam: SubtaskParam){
        subTaskList.add(subtaskParam)
        notifyItemInserted(subTaskList.lastIndex)
    }

    fun updateChecked(isChecked : Boolean, position: Int){
        subTaskList[position].isChecked = isChecked
        notifyItemChanged(position)
    }

    fun deleteSubtask(position: Int){
        subTaskList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getAdapterList() : List<SubtaskParam>{
        return subTaskList
    }
    interface OnItemViewClickListener{
        fun onCheckboxItemClick(view: View, position: Int, isChecked : Boolean)
        fun onDeleteButtonClick(view: View, position: Int)
    }

    fun setOnItemViewClickListener(listener : OnItemViewClickListener){
        this.listener = listener
    }

    fun setList(newList : List<SubtaskParam>){
        subTaskList.clear()
        subTaskList.addAll(newList)
        notifyDataSetChanged()
    }

}