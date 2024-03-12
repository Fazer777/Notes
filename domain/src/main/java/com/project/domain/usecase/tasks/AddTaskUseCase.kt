package com.project.domain.usecase.tasks

import com.project.domain.dto.TaskDTO
import com.project.domain.models.TaskInterim
import com.project.domain.repository.ITaskRepository

class AddTaskUseCase(private val taskRepository: ITaskRepository) {
    suspend fun execute(taskInterim: TaskInterim) : Int{
        return taskRepository.addTask(taskInterim =  taskInterim)
    }
}