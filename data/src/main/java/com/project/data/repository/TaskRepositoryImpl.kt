package com.project.data.repository

import com.project.data.database.dao.dao_interface.ISubTaskDao
import com.project.data.database.dao.dao_interface.ITaskDao
import com.project.data.models.SubTask
import com.project.data.models.Task
import com.project.domain.dto.TaskDTO
import com.project.domain.models.SubTaskInterim
import com.project.domain.models.TaskInterim
import com.project.domain.repository.ITaskRepository
import java.util.stream.Collectors

class TaskRepositoryImpl(
    private val taskDao : ITaskDao,
    private val subTaskDao : ISubTaskDao,

) : ITaskRepository {
    override fun addTask(taskInterim: TaskInterim): TaskDTO {
        val task = mapToTask(taskInterim)
        val taskId = taskDao.addTask(task)
        var subtaskIdList: List<Int>? = emptyList()
        task.subTasks?.let {
            subtaskIdList = subTaskDao.addSubTask(task.subTasks!!)
        }

        return TaskDTO(
            taskId = taskId,
            subtaskIdList = subtaskIdList
        )
    }

    override fun updateTask(taskId: Int, taskInterim: TaskInterim) {
        TODO("Not yet implemented")
    }

    override fun deleteTask(taskId: Int) {
        subTaskDao.deleteSubTask(taskId)
        taskDao.deleteTask(taskId)
    }

    override fun getTasks(): List<TaskInterim> {
        val taskList = taskDao.getTasks()
        val subtaskList = subTaskDao.getSubTasks()

        taskList.forEach { task->
            val subTaskList = mutableListOf<SubTask>()
            subtaskList.forEach {subtask->
                if (task.id == subtask.taskId){
                    subTaskList.add(subtask)
                }
            }
            task.subTasks = subTaskList
        }

        return taskList
            .parallelStream()
            .map{it -> mapToTaskInterim(it)}
            .collect(Collectors.toList())
    }

    private fun mapToTask(taskInterim: TaskInterim) : Task{
        return Task(
            id = taskInterim.id,
            title = taskInterim.title,
            description = taskInterim.description,
            subTasks = taskInterim.subTasks?.let { list ->
                list.map { subtaskInterim ->
                    SubTask(
                        id = subtaskInterim.id,
                        title = subtaskInterim.title,
                        isChecked = subtaskInterim.isChecked,
                        taskId = subtaskInterim.taskId
                    )
                }
            },
            color = taskInterim.color,
            appointedDate = taskInterim.appointedDate,
            completionDate = taskInterim.completionDate,
            isChecked = taskInterim.isChecked
        )
    }

    private fun mapToTaskInterim (task : Task) : TaskInterim{
        return TaskInterim(
            id = task.id,
            title = task.title,
            description = task.description,
            subTasks = task.subTasks?.let { list ->
                list.map { subtask ->
                    SubTaskInterim(
                        id = subtask.id,
                        title = subtask.title,
                        isChecked = subtask.isChecked,
                        taskId = subtask.taskId
                    )
                }
            },
            color = task.color,
            appointedDate = task.appointedDate,
            completionDate = task.completionDate,
            isChecked = task.isChecked
        )
    }

}
