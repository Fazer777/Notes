package com.project.domain.usecase.notes

import com.project.domain.repository.INoteRepository

class DeleteNoteUseCase(val noteRepository: INoteRepository) {
    suspend fun execute(itemIndex : Int){
        noteRepository.deleteNote(itemIndex)
    }
}