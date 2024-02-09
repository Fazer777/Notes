package com.project.data.database.dao

import com.project.data.models.Note

interface NoteDao {
    fun addNote(note : Note)
    fun deleteNote(itemIndex : Int)
    fun updateNote(note : Note)
    fun readAllData() : List<Note>
}