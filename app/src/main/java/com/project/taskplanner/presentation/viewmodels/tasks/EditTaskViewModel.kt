package com.project.taskplanner.presentation.viewmodels.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.domain.models.task.TaskParam
import com.project.domain.usecase.task.AddTaskUseCase
import com.project.domain.usecase.task.UpdateTaskUseCase
import kotlinx.coroutines.launch

class EditTaskViewModel(
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase
) : ViewModel() {
    fun addTask(taskParam : TaskParam){
        viewModelScope.launch {
            addTaskUseCase.execute(taskParam = taskParam)
        }
    }

    fun updateTask(updatedTask : TaskParam){
        viewModelScope.launch {
            updateTaskUseCase.execute(updatedTask = updatedTask)
        }
    }
}