package com.project.data.repository

import com.project.data.database.dao.ITaskDao
import com.project.data.mappers.TaskMapper
import com.project.domain.models.task.TaskParam
import com.project.domain.repository.task.ITaskRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class TaskRepositoryImpl(
    private val taskDao: ITaskDao,
    private val dispatcher: CoroutineDispatcher
) : ITaskRepository {
    override suspend fun addTask(taskParam: TaskParam) {
        withContext(dispatcher){
            taskDao.addTask(TaskMapper.mapToData(taskParam = taskParam))
        }
    }

    override suspend fun updateTask(taskParam: TaskParam) {
        withContext(dispatcher){
            taskDao.updateTask(TaskMapper.mapToData(taskParam = taskParam))
        }
    }

    override suspend fun updateTaskChecked(
        taskId: Int,
        checked: Boolean,
        completionDate: LocalDate?
    ) {
        withContext(dispatcher){
            val dateStr : String? = completionDate?.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
            taskDao.updateTaskChecked(taskId = taskId, checked = checked, completionDate = dateStr)
        }
    }

    override suspend fun deleteTask(taskParam: TaskParam) {
        withContext(dispatcher){
            taskDao.deleteTask(TaskMapper.mapToData(taskParam = taskParam))
        }
    }

    override fun getTasks(): Flow<List<TaskParam>> {
        return taskDao.getTasks().map { list -> list.map { entity -> TaskMapper.mapToDomain(entity) } }
    }
}
