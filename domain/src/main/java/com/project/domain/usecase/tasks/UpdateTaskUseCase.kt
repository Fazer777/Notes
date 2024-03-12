package com.project.domain.usecase.tasks

import com.project.domain.models.TaskInterim
import com.project.domain.repository.ITaskRepository

class UpdateTaskUseCase(private val taskRepository: ITaskRepository) {
    suspend fun execute(newTask:TaskInterim){
        taskRepository.updateTask(taskId = newTask.id, newTask = newTask)
    }
}