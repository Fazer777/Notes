package com.project.taskplanner.presentation.viewmodels.tasks

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.data.database.dao.SubtaskDaoImpl
import com.project.data.database.dao.TaskDaoImpl
import com.project.data.repository.TaskRepositoryImpl
import com.project.domain.usecase.tasks.AddTaskUseCase
import com.project.domain.usecase.tasks.DeleteTaskUseCase
import com.project.domain.usecase.tasks.GetTaskUseCase
import com.project.domain.usecase.tasks.UpdateTaskUseCase

class TaskInProgressViewModelFactory(context : Context) : ViewModelProvider.Factory {

    private val taskRepository by lazy {
        TaskRepositoryImpl(
            TaskDaoImpl(context = context),
            SubtaskDaoImpl(context = context)
        )
    }

    private val addTaskUseCase by lazy {
        AddTaskUseCase(taskRepository)
    }

    private val updateTaskUseCase by lazy {
        UpdateTaskUseCase(taskRepository)
    }

    private val deleteTaskUseCase by lazy {
        DeleteTaskUseCase(taskRepository)
    }

    private val getTaskUseCase by lazy {
        GetTaskUseCase(taskRepository)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TaskInProgressVM(
            addTaskUseCase =  addTaskUseCase,
            updateTaskUseCase = updateTaskUseCase,
            deleteTaskUseCase = deleteTaskUseCase,
            getTaskUseCase = getTaskUseCase
        ) as T
    }
}