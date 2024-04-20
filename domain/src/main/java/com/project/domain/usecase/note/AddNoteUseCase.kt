package com.project.domain.usecase.note

import com.project.domain.models.note.NoteParam
import com.project.domain.repository.note.INoteRepository

class AddNoteUseCase(private val noteRepository: INoteRepository) {
    suspend fun execute(noteParam: NoteParam) {
        noteRepository.addNote(noteParam)
    }
}