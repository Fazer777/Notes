package com.project.domain.usecase.notes

import com.project.domain.models.NoteInterim
import com.project.domain.repository.INoteRepository

class UpdateNoteUseCase(val noteRepository: INoteRepository) {

    fun execute(noteInterim: NoteInterim) {
        noteRepository.updateNote(noteInterim)
    }
}