package com.project.taskplanner.presentation.adapters.category

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.project.taskplanner.R

class ColorSpinnerAdapter(private val colorList : Array<String>) : BaseAdapter() {

    override fun getCount(): Int {
        return colorList.count()
    }

    override fun getItem(position: Int): Any {
        return colorList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view : View? = convertView
        if(view == null){
            view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.color_spinner_item, parent, false)
        }
        val viewColor = view?.findViewById<View>(R.id.id_item_color_blob)
        viewColor?.background?.setTint(Color.parseColor(colorList[position]))
        return view
    }
}