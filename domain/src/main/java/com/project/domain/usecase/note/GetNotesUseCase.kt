package com.project.domain.usecase.note

import com.project.domain.models.note.NoteParam
import com.project.domain.repository.note.INoteRepository
import kotlinx.coroutines.flow.Flow

class GetNotesUseCase(private val noteRepository: INoteRepository) {
    fun execute() : Flow<List<NoteParam>> {
        return  noteRepository.getNotes()
    }
}