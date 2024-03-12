package com.project.taskplanner.presentation.viewmodels.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.domain.models.CategoryInterim
import com.project.domain.usecase.categories.GetCategoriesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddNoteVM (
    private val getCategoriesUseCase: GetCategoriesUseCase
): ViewModel() {

    private val isVisibleCheckMutableLive = MutableLiveData<Boolean>()
    val isVisibleCheckLive : LiveData<Boolean> = isVisibleCheckMutableLive

    private val categoriesMutableLive = MutableLiveData<List<CategoryInterim>>()
    val categoriesLive : LiveData<List<CategoryInterim>> = categoriesMutableLive

    init {
        isVisibleCheckMutableLive.value = false
        CoroutineScope(Dispatchers.IO).launch {
            categoriesMutableLive.postValue(getCategoriesUseCase.execute())
        }
    }

    fun textChanged(text : String) : Unit{
        isVisibleCheckMutableLive.value = text.isNotBlank()
    }
}