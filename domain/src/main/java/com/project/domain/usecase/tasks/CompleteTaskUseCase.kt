package com.project.domain.usecase.tasks

import com.project.domain.repository.ITaskRepository
import java.time.LocalDate

class CompleteTaskUseCase (private val taskRepository: ITaskRepository) {
    suspend fun execute(taskId : Int, flag : Boolean, completionDate : LocalDate){
        taskRepository.updateTaskChecked(
            taskId = taskId,
            checked =  flag,
            completionDate = completionDate
        )
    }
}