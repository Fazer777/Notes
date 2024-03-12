package com.project.domain.usecase.tasks.subtask

import com.project.domain.models.SubTaskInterim
import com.project.domain.repository.ISubtaskRepository

class AddSubtaskUseCase(private val subtaskRepository: ISubtaskRepository) {
    suspend fun execute(taskId : Int, subtaskList : List<SubTaskInterim>) : List<Int>{
        return subtaskRepository.addSubtask(taskId = taskId, subTasks = subtaskList)
    }
}