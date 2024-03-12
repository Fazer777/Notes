package com.project.data.repository

import com.project.data.database.dao.dao_interface.ISubTaskDao
import com.project.data.models.SubTask
import com.project.domain.models.SubTaskInterim
import com.project.domain.repository.ISubtaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SubtaskRepositoryImpl(private val subtaskDao : ISubTaskDao) : ISubtaskRepository {
    override suspend fun addSubtask(taskId: Int, subTasks: List<SubTaskInterim>) : List<Int> = withContext(Dispatchers.IO) {

        return@withContext subtaskDao.addSubTask(taskId =  taskId, subTasks = subTasks.map { it -> mapToSubtask(it)})
    }

    override suspend fun deleteSubtask(taskId: Int) = withContext(Dispatchers.IO) {
        subtaskDao.deleteSubTask(taskId = taskId)
    }

    override suspend fun getSubtasks(): List<SubTaskInterim>  = withContext(Dispatchers.IO){
        return@withContext subtaskDao.getSubTasks().map { it -> mapToSubtaskInterim(it) }
    }

    private fun mapToSubtask(subTaskInterim: SubTaskInterim) : SubTask{
        return SubTask(
            id = subTaskInterim.id,
            title = subTaskInterim.title,
            isChecked = subTaskInterim.isChecked,
            taskId = subTaskInterim.taskId
        )
    }

    private fun mapToSubtaskInterim(subTask: SubTask) : SubTaskInterim{
        return SubTaskInterim(
            id = subTask.id,
            title = subTask.title,
            isChecked = subTask.isChecked,
            taskId = subTask.taskId
        )
    }

}