package com.project.taskplanner.presentation.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.TextView
import com.project.taskplanner.R
import com.project.taskplanner.presentation.activities.CategoryActivity


class GridViewDialog(
    private val mContext : Context,
    private var colorList : Array<String>
) : Dialog(mContext) {

    private lateinit var gridView :GridView
    private var mIndex : Int = 1
    private var gridViewAdapter = GridViewAdapter(mContext)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.layout_grid_view)

        gridView = findViewById(R.id.grid_view)

        gridView.choiceMode = GridView.CHOICE_MODE_SINGLE

        gridViewAdapter.addAll(colorList.asList())
        gridView.adapter = gridViewAdapter
        gridView.setItemChecked(mIndex, true);

        gridView.setOnItemClickListener { parent, view, position, id ->
            (mContext as CategoryActivity).setTextViewColor(colorList[position])
            dismiss()
        }

    }

    inner class GridViewAdapter(
        context : Context
    ) : ArrayAdapter<String>(context, android.R.layout.simple_list_item_1){

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var view = convertView
            if (view == null){
                view = LayoutInflater.from(context)
                    .inflate(R.layout.color_spinner_item, parent, false)
            }
            val tvColorBlob : View? = view?.findViewById<View>(R.id.id_item_color_blob)
            tvColorBlob?.background?.setTint(Color.parseColor(getItem(position)))
            return view!!
        }
    }

}