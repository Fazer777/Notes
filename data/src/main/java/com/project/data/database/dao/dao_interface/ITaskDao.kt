package com.project.data.database.dao.dao_interface

import com.project.data.models.Task

interface ITaskDao {
    fun addTask(task : Task) : Int

    fun updateTask(id : Int, task: Task)


    fun deleteTask(taskId : Int)

    fun getTasks() : List<Task>
}