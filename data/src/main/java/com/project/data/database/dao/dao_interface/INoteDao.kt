package com.project.data.database.dao.dao_interface

import com.project.data.models.Note

interface INoteDao {
    suspend fun addNote(note : Note)
    suspend fun deleteNote(itemIndex : Int)
    suspend fun updateNote(note : Note)
    suspend fun readAllData() : List<Note>
}