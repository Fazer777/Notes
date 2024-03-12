package com.project.domain.repository

import com.project.domain.models.NoteInterim

interface INoteRepository {

    suspend fun addNote(noteInterim : NoteInterim) : Unit

    suspend fun updateNote(noteInterim : NoteInterim) : Unit

    suspend fun deleteNote(itemIndex : Int) : Unit

    suspend fun getNotes() : List<NoteInterim>

}