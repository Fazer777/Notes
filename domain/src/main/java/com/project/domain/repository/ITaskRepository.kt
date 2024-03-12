package com.project.domain.repository

import com.project.domain.dto.TaskDTO
import com.project.domain.models.TaskInterim
import java.time.LocalDate

interface ITaskRepository {
    suspend fun addTask(taskInterim: TaskInterim) : Int

    suspend fun updateTask(taskId : Int, newTask: TaskInterim)

    suspend fun updateTaskChecked(taskId : Int, checked : Boolean, completionDate : LocalDate)

    suspend fun deleteTask(taskId : Int)

    suspend fun getTasks() : List<TaskInterim>

}