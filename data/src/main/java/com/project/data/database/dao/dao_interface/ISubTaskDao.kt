package com.project.data.database.dao.dao_interface

import com.project.data.models.SubTask

interface ISubTaskDao {
    fun addSubTask(subTasks : List<SubTask>) : List<Int>

    fun deleteSubTask (taskId : Int)

    fun getSubTasks() : List<SubTask>
}