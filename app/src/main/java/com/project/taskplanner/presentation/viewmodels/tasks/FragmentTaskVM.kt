package com.project.taskplanner.presentation.viewmodels.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.data.models.Task
import com.project.domain.models.TaskInterim
import com.project.domain.usecase.tasks.AddTaskUseCase
import com.project.domain.usecase.tasks.CompleteTaskUseCase
import com.project.domain.usecase.tasks.DeleteTaskUseCase
import com.project.domain.usecase.tasks.GetTaskUseCase
import com.project.domain.usecase.tasks.UpdateTaskUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class FragmentTaskVM(
    private val addTaskUseCase: AddTaskUseCase,
    private val getTaskUseCase: GetTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase
) : ViewModel() {

    suspend fun onAddTaskButtonClicked(createdTask : TaskInterim) : Int{
       return addTaskUseCase.execute(taskInterim =  createdTask)
    }

    suspend fun onUpdateTaskButtonClicked(updatedTask: TaskInterim){
        updateTaskUseCase.execute(newTask = updatedTask)
    }

    suspend fun onDeleteTaskButtonClicked(taskId : Int){
        deleteTaskUseCase.execute(taskId = taskId)
    }

    suspend fun onThreeDotButtonClicked(taskId: Int, flag : Boolean, completionDate : LocalDate){
        completeTaskUseCase.execute(
            taskId = taskId,
            flag = flag,
            completionDate = completionDate
        )
    }

    suspend fun getTasks() : List<TaskInterim> {
        return getTaskUseCase.execute()
    }
}