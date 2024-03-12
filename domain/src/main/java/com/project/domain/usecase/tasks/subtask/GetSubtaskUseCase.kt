package com.project.domain.usecase.tasks.subtask

import com.project.domain.models.SubTaskInterim
import com.project.domain.repository.ISubtaskRepository
import com.project.domain.repository.ITaskRepository

class GetSubtaskUseCase(private val subtaskRepository: ISubtaskRepository) {
    suspend fun execute() : List<SubTaskInterim>{
        return subtaskRepository.getSubtasks()
    }
}