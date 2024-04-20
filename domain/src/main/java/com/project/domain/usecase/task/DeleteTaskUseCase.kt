package com.project.domain.usecase.task

import com.project.domain.models.task.TaskParam
import com.project.domain.repository.task.ITaskRepository

class DeleteTaskUseCase(private val taskRepository: ITaskRepository) {
    suspend fun execute(taskParam: TaskParam){
        taskRepository.deleteTask(taskParam = taskParam)
    }
}