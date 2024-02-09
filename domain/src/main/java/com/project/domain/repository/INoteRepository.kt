package com.project.domain.repository

import com.project.domain.models.NoteInterim

interface INoteRepository {

    fun addNote(noteInterim : NoteInterim) : Unit

    fun updateNote(noteInterim : NoteInterim) : Unit

    fun deleteNote(itemIndex : Int) : Unit

    fun getNotes() : List<NoteInterim>

}