package com.project.taskplanner.presentation.adapters.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.project.domain.models.category.CategoryParam
import com.project.taskplanner.R

class CategorySpinnerAdapter(): BaseAdapter() {

    private var categoryList : List<CategoryParam> = listOf()

    override fun getCount(): Int {
        return categoryList.count()
    }

    override fun getItem(position: Int): Any {
        return categoryList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun getItemPositionByName(nameCategory : String) : Int{
        for (i in 0..<categoryList.count()){
            if (nameCategory == categoryList[i].name){
                return i
            }
        }
        return 0
    }

    fun setList(newList : List<CategoryParam>){
        categoryList = newList
        notifyDataSetChanged()
    }



    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view : View? = convertView
        if (view == null){
            val layoutInflater = LayoutInflater.from(parent?.context)
            view = layoutInflater.inflate(R.layout.item_category_spinner, parent, false)
        }
        val imgView : ImageView? = view?.findViewById(R.id.id_shape_img_view_category)
        val textView : TextView? = view?.findViewById(R.id.id_tv_category_name)
        imgView?.setColorFilter(categoryList[position].color)
        textView?.text = categoryList[position].name
        return view
    }
}
