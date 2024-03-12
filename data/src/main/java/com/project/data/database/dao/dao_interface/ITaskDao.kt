package com.project.data.database.dao.dao_interface

import com.project.data.models.Task
import java.time.LocalDate

interface ITaskDao {
    suspend fun addTask(task : Task) : Int
   suspend fun updateTask(taskId: Int, task: Task)
   suspend fun updateTaskChecked(taskId: Int, checked : Boolean, completionDate:LocalDate)
    suspend fun deleteTask(taskId : Int)
    suspend fun getTasks() : List<Task>
}