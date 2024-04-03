package com.project.taskplanner.presentation.adapters.note

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.domain.models.NoteInterim
import com.project.taskplanner.R
import com.project.taskplanner.databinding.ItemNoteRecyclerviewBinding
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class RecyclerViewNoteAdapter() : RecyclerView.Adapter<RecyclerViewNoteAdapter.ViewHolder>() {

    private var listener: MyOnItemClickListener? = null
    private val noteList = ArrayList<NoteInterim>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemNoteRecyclerviewBinding.bind(itemView)

        fun bind(noteInterim : NoteInterim) = with(binding){
            idTvTextNotePreview.text = noteInterim.noteDescription
            idCvColorNote.setCardBackgroundColor(noteInterim.category.color)
            idTvDateNotePreview.text = noteInterim
                .noteDate
                .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
            textViewNoteCategory.text = noteInterim.category.name

            itemView.setOnClickListener {
                listener?.let {
                    val position : Int = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        listener?.onItemClick(itemView, position)
                    }
                }
            }

            itemView.setOnLongClickListener{
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(noteList[position]);
    }

    override fun getItemCount(): Int {
        return noteList.count()
    }

    fun addNote(noteInterim : NoteInterim){
        noteList.add(noteInterim)
        notifyItemInserted(noteList.count() - 1)
    }

    fun deleteNote(indexElem : Int){
        noteList.removeAt(indexElem)
        notifyItemRemoved(indexElem)
        updateItemIndex(indexElem)
    }

    fun updateNote(indexElem : Int, note : NoteInterim){
        noteList[indexElem] = note
        notifyItemChanged(indexElem)
    }

    private fun updateItemIndex(itemIndex : Int){
        noteList.parallelStream()
            .filter { it.itemIndex > itemIndex}
            .forEach { it -> it.itemIndex -=1 }
    }

    public fun setAdapterList(newList : List<NoteInterim>){
        noteList.clear()
        noteList.addAll(newList)
        notifyDataSetChanged()
    }

    public fun getItem(position : Int) : NoteInterim{
        return noteList[position]
    }

    interface MyOnItemClickListener{
        fun onItemClick(itemView: View, position : Int)
        fun onItemLongClick(itemView: View, position: Int)
    }

    public fun setOnItemClickListener(listener: MyOnItemClickListener){
        this.listener = listener
    }


}