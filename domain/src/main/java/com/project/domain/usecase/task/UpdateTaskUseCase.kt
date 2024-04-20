package com.project.domain.usecase.task

import com.project.domain.models.task.TaskParam
import com.project.domain.repository.task.ITaskRepository

class UpdateTaskUseCase(private val taskRepository: ITaskRepository) {
    suspend fun execute(updatedTask : TaskParam){
        taskRepository.updateTask(taskParam = updatedTask)
    }
}