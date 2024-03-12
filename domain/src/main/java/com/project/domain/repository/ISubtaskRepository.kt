package com.project.domain.repository

import com.project.domain.models.SubTaskInterim

interface ISubtaskRepository {
    suspend fun addSubtask(taskId : Int, subTasks: List<SubTaskInterim>) : List<Int>
    suspend fun deleteSubtask(taskId: Int)
    suspend fun getSubtasks() : List<SubTaskInterim>
}