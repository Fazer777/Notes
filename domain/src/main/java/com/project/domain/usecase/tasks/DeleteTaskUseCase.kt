package com.project.domain.usecase.tasks

import com.project.domain.repository.ITaskRepository

class DeleteTaskUseCase(private val taskRepository : ITaskRepository) {
    fun execute(taskId : Int){
        taskRepository.deleteTask(taskId)
    }
}