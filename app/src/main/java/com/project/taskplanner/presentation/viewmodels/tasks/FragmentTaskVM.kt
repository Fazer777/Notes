package com.project.taskplanner.presentation.viewmodels.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.project.domain.models.task.TaskParam
import com.project.domain.usecase.task.CompleteTaskUseCase
import com.project.domain.usecase.task.DeleteTaskUseCase
import com.project.domain.usecase.task.GetTaskUseCase
import com.project.taskplanner.presentation.fragments.tasks.LayoutEnum
import kotlinx.coroutines.launch
import java.time.LocalDate

class FragmentTaskVM(
    private val getTaskUseCase: GetTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase
) : ViewModel() {

    private val layoutStateMutableLive = MutableLiveData<LayoutEnum>()
    val layoutStateLive : LiveData<LayoutEnum> = layoutStateMutableLive

     fun deleteTask(taskParam: TaskParam){
        viewModelScope.launch {
            deleteTaskUseCase.execute(taskParam = taskParam)
        }
    }

    fun completeTask(taskId: Int, isChecked : Boolean, completionDate : LocalDate?){
        viewModelScope.launch {
            completeTaskUseCase.execute(taskId = taskId, flag = isChecked, completionDate = completionDate)
        }
    }

    fun getTasks() : LiveData<List<TaskParam>> {
        return getTaskUseCase.execute().asLiveData()
    }

    fun changeLayoutState(state : LayoutEnum){
        layoutStateMutableLive.value = state
    }
}