package com.project.data.database.dao.dao_interface

import com.project.data.models.Note

interface INoteDao {
    fun addNote(note : Note)
    fun deleteNote(itemIndex : Int)
    fun updateNote(note : Note)
    fun readAllData() : List<Note>
}