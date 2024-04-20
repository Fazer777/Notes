package com.project.domain.repository.note

import com.project.domain.models.note.NoteParam
import kotlinx.coroutines.flow.Flow
interface INoteRepository {

    suspend fun addNote(noteInterim : NoteParam)

    suspend fun updateNote(noteInterim : NoteParam)

    suspend fun deleteNote(noteInterim : NoteParam)

    fun getNotes() : Flow<List<NoteParam>>

}