package com.project.domain.usecase.tasks

import com.project.domain.dto.TaskDTO
import com.project.domain.models.TaskInterim
import com.project.domain.repository.ITaskRepository

class AddTaskUseCase(private val taskRepository: ITaskRepository) {
    fun execute(taskInterim: TaskInterim) : TaskDTO {
       return taskRepository.addTask(taskInterim)
    }
}