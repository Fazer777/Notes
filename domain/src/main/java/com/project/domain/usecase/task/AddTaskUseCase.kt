package com.project.domain.usecase.task

import com.project.domain.models.task.TaskParam
import com.project.domain.repository.task.ITaskRepository

class AddTaskUseCase(private val taskRepository: ITaskRepository) {
    suspend fun execute(taskParam: TaskParam) {
        taskRepository.addTask(taskParam = taskParam)
    }
}