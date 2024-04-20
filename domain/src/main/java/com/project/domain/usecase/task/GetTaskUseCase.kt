package com.project.domain.usecase.task

import com.project.domain.models.task.TaskParam
import com.project.domain.repository.task.ITaskRepository
import kotlinx.coroutines.flow.Flow

class GetTaskUseCase(private val taskRepository: ITaskRepository) {
    fun execute() : Flow<List<TaskParam>>{
        return taskRepository.getTasks()
    }
}