package com.project.domain.usecase.task

import com.project.domain.repository.task.ITaskRepository
import java.time.LocalDate

class CompleteTaskUseCase (private val taskRepository: ITaskRepository) {
    suspend fun execute(taskId : Int, flag : Boolean, completionDate : LocalDate?){
        taskRepository.updateTaskChecked(
            taskId = taskId,
            checked =  flag,
            completionDate = completionDate
        )
    }
}