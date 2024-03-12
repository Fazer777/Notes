package com.project.taskplanner.presentation.viewmodels.tasks.subtask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.domain.models.SubTaskInterim
import com.project.domain.usecase.tasks.subtask.AddSubtaskUseCase
import com.project.domain.usecase.tasks.subtask.GetSubtaskUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SubtaskViewModel(
    private val addSubtaskUseCase: AddSubtaskUseCase,
    private val getSubtaskUseCase: GetSubtaskUseCase
) : ViewModel() {

    private val subtaskIdList = MutableLiveData<List<Int>>()
    var subtaskIdListLive : LiveData<List<Int>> = subtaskIdList

     suspend fun onAddTaskButtonClicked(taskId : Int, subtaskList : List<SubTaskInterim>?) : List<Int>? {
         return if (subtaskList != null) {
             addSubtaskUseCase.execute(taskId, subtaskList)
         } else null
     }

    suspend fun getSubtasks() : List<SubTaskInterim>{
        return getSubtaskUseCase.execute()
    }

}