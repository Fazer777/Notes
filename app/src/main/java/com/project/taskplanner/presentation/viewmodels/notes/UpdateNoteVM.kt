package com.project.taskplanner.presentation.viewmodels.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.domain.models.CategoryInterim
import com.project.domain.usecase.categories.GetCategoriesUseCase

class UpdateNoteVM(
    private val getCategoriesUseCase : GetCategoriesUseCase
) : ViewModel() {

    private val isVisibleCheckMutableLive = MutableLiveData<Boolean>()
    val isVisibleCheckLive : LiveData<Boolean> = isVisibleCheckMutableLive

    private val categoriesMutableLive = MutableLiveData<List<CategoryInterim>>()
    val categoriesLive : LiveData<List<CategoryInterim>> = categoriesMutableLive

    init {
        isVisibleCheckMutableLive.value = false
        categoriesMutableLive.value = getCategoriesUseCase.execute()
    }

    fun textChanged(text : String) : Unit{
        isVisibleCheckMutableLive.value = text.isNotBlank()
    }
}