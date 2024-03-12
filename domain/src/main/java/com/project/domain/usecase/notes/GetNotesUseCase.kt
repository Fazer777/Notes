package com.project.domain.usecase.notes

import com.project.domain.models.NoteInterim
import com.project.domain.repository.INoteRepository

class GetNotesUseCase(private val noteRepository: INoteRepository) {
    suspend fun execute() : ArrayList<NoteInterim>{
        return  noteRepository.getNotes() as ArrayList<NoteInterim>
    }
}