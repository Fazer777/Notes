package com.project.domain.repository.task

import com.project.domain.models.task.TaskParam
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ITaskRepository {
    suspend fun addTask(taskParam: TaskParam)

    suspend fun updateTask(taskParam: TaskParam)

    suspend fun updateTaskChecked(taskId : Int, checked : Boolean, completionDate : LocalDate?)

    suspend fun deleteTask(taskParam: TaskParam)

    fun getTasks() : Flow<List<TaskParam>>

}