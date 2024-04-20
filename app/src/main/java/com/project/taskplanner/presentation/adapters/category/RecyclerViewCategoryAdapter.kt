package com.project.taskplanner.presentation.adapters.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.project.domain.models.category.CategoryParam
import com.project.taskplanner.R
import com.project.taskplanner.databinding.ItemCategoryRecyclerviewBinding
import java.util.stream.Collectors

class RecyclerViewCategoryAdapter : RecyclerView.Adapter<RecyclerViewCategoryAdapter.MyViewHolder>() {

    private val categoryList = ArrayList<CategoryParam>()
    private var listener : MyOnItemClickListener? = null

    inner class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        private val binding = ItemCategoryRecyclerviewBinding.bind(itemView)
        fun bind(categoryParam: CategoryParam) = with(binding){
            textview.text = categoryParam.name
            cardViewColor.setCardBackgroundColor(categoryParam.color)

            itemView.setOnLongClickListener {
                listener?.let {
                    val position : Int = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        listener?.onItemLongClick(itemView, position)
                    }
                }
                return@setOnLongClickListener true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_recyclerview, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }

    override fun getItemCount(): Int {
        return categoryList.count()
    }

    fun checkDuplicate(nameCategory : String) : Boolean{
        return categoryList
            .parallelStream()
            .map { cat-> cat.name }
            .collect(Collectors.toList())
            .contains(nameCategory)
    }

    fun setAdapterList(newList : List<CategoryParam>){
        categoryList.clear()
        categoryList.addAll(newList)
        notifyDataSetChanged()
    }

    fun getItem(position: Int) : CategoryParam {
        return categoryList[position]
    }

    interface MyOnItemClickListener{
        fun onItemLongClick(itemView : View, position: Int)
    }

    fun setOnItemClickListener(listener : MyOnItemClickListener){
        this.listener = listener
    }

}