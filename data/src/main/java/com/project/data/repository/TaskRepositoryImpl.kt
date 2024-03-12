package com.project.data.repository

import com.project.data.database.dao.dao_interface.ISubTaskDao
import com.project.data.database.dao.dao_interface.ITaskDao
import com.project.data.models.Task
import com.project.domain.models.TaskInterim
import com.project.domain.repository.ITaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

class TaskRepositoryImpl(
    private val taskDao: ITaskDao,
    private val subTaskDao: ISubTaskDao,

    ) : ITaskRepository {
    override suspend fun addTask(taskInterim: TaskInterim): Int = withContext(Dispatchers.IO){
        val task = mapToTask(taskInterim)
        return@withContext taskDao.addTask(task)
    }

    override suspend fun updateTask(taskId: Int, newTask: TaskInterim): Unit = withContext(Dispatchers.IO){
        val task = mapToTask(newTask)
        taskDao.updateTask(taskId = taskId, task = task)
        subTaskDao.deleteSubTask(taskId)
        task.subTasks?.let {subtaskList ->
            subTaskDao.addSubTask(taskId, subtaskList)
        }
    }

    override suspend fun updateTaskChecked(taskId: Int, checked: Boolean, completionDate : LocalDate) = withContext(Dispatchers.IO) {
        taskDao.updateTaskChecked(
            taskId = taskId,
            checked = checked,
            completionDate = completionDate)
    }

    override suspend fun deleteTask(taskId: Int) = withContext(Dispatchers.IO) {
        subTaskDao.deleteSubTask(taskId)
        taskDao.deleteTask(taskId)
    }

    override suspend fun getTasks(): List<TaskInterim>  = withContext(Dispatchers.IO) {
        val taskList = taskDao.getTasks()
        return@withContext taskList.map { it -> mapToTaskInterim(it) }
    }


    private fun mapToTask(taskInterim: TaskInterim): Task {
        return Task(
            id = taskInterim.id,
            title = taskInterim.title,
            description = taskInterim.description,
            subTasks = null,
            color = taskInterim.color,
            appointedDate = taskInterim.appointedDate,
            completionDate = taskInterim.completionDate,
            isChecked = taskInterim.isChecked
        )
    }

    private fun mapToTaskInterim(task: Task): TaskInterim {
        return TaskInterim(
            id = task.id,
            title = task.title,
            description = task.description,
            subTasks = null,
            color = task.color,
            appointedDate = task.appointedDate,
            completionDate = task.completionDate,
            isChecked = task.isChecked
        )
    }

}
