package com.project.taskplanner.presentation.viewmodels.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.domain.dto.TaskDTO
import com.project.domain.models.TaskInterim
import com.project.domain.usecase.tasks.AddTaskUseCase
import com.project.domain.usecase.tasks.DeleteTaskUseCase
import com.project.domain.usecase.tasks.GetTaskUseCase
import com.project.domain.usecase.tasks.UpdateTaskUseCase

class TaskInProgressVM(
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val getTaskUseCase: GetTaskUseCase
) : ViewModel() {

    private val taskListMutableLive = MutableLiveData<ArrayList<TaskInterim>>()
    val taskListLive : LiveData<ArrayList<TaskInterim>> = taskListMutableLive

    init {
        getTasks()
    }

    fun onAddBtnClicked(taskInterim: TaskInterim) : TaskDTO {
        return addTaskUseCase.execute(taskInterim)
    }

    fun onDelBtnClicked(taskId : Int){
        deleteTaskUseCase.execute(taskId)
    }

    private fun getTasks(){
        taskListMutableLive.value = getTaskUseCase.execute() as ArrayList<TaskInterim>
    }

}