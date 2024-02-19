package com.project.domain.repository

import com.project.domain.dto.TaskDTO
import com.project.domain.models.TaskInterim

interface ITaskRepository {
    fun addTask(taskInterim: TaskInterim) : TaskDTO

    fun updateTask(taskId : Int, taskInterim: TaskInterim)

    fun deleteTask(taskId : Int)

    fun getTasks() : List<TaskInterim>
}