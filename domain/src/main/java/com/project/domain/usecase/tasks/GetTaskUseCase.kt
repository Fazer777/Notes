package com.project.domain.usecase.tasks

import com.project.domain.models.TaskInterim
import com.project.domain.repository.ITaskRepository

class GetTaskUseCase(private val taskRepository: ITaskRepository) {
    suspend fun execute() : List<TaskInterim>{
        return taskRepository.getTasks()
    }
}