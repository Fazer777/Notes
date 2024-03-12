package com.project.data.database.dao.dao_interface

import com.project.data.models.SubTask

interface ISubTaskDao {
    suspend fun addSubTask(taskId : Int, subTasks : List<SubTask>) : List<Int>

    suspend fun deleteSubTask (taskId : Int)

    suspend fun getSubTasks() : List<SubTask>
}