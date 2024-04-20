package com.project.taskplanner.presentation.viewmodels.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.project.domain.models.category.CategoryParam
import com.project.domain.models.note.NoteParam
import com.project.domain.usecase.category.GetCategoriesUseCase
import com.project.domain.usecase.note.AddNoteUseCase
import kotlinx.coroutines.launch

class AddNoteVM (
    private val addNoteUseCase: AddNoteUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
): ViewModel() {

    private val isVisibleCheckMutableLive = MutableLiveData<Boolean>()
    val isVisibleCheckLive : LiveData<Boolean> = isVisibleCheckMutableLive

    val categories : LiveData<List<CategoryParam>> = getCategoriesLive()

    init {
        isVisibleCheckMutableLive.value = false
    }

    fun textChanged(text : String) : Unit{
        isVisibleCheckMutableLive.value = text.isNotBlank()
    }

    fun addNote(noteParam: NoteParam){
        viewModelScope.launch {
            addNoteUseCase.execute(noteParam = noteParam)
        }
    }

    private fun getCategoriesLive() : LiveData<List<CategoryParam>>
    {
        return getCategoriesUseCase.execute().asLiveData()
    }
}