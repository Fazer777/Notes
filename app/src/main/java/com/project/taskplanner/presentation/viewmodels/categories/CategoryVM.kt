package com.project.taskplanner.presentation.viewmodels.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.project.domain.models.category.CategoryParam
import com.project.domain.usecase.category.AddCategoryUseCase
import com.project.domain.usecase.category.DeleteCategoryUseCase
import com.project.domain.usecase.category.GetCategoriesUseCase
import com.project.domain.usecase.category.UpdateDeletedCategoryUseCase
import kotlinx.coroutines.launch

class CategoryVM(
    private val addCategoryUseCase: AddCategoryUseCase,
    private val updateDeletedCategoryUseCase: UpdateDeletedCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    val categories: LiveData<List<CategoryParam>> = getCategoriesLive()

    fun addCategory(categoryParam: CategoryParam) {
        viewModelScope.launch {
            addCategoryUseCase.execute(categoryParam = categoryParam)
        }
    }

    fun deleteCategory(categoryParam: CategoryParam) {
        viewModelScope.launch {
            deleteCategoryUseCase.execute(categoryParam = categoryParam)
        }
    }

    fun updateNotesWithDeletedCategory(defaultCategoryId: Long, deletedCategoryId: Long) {
        viewModelScope.launch {
            updateDeletedCategoryUseCase.execute(
                defaultCategoryId = defaultCategoryId,
                deletedCategoryId = deletedCategoryId
            )
        }
    }

    private fun getCategoriesLive(): LiveData<List<CategoryParam>> {
        return getCategoriesUseCase.execute().asLiveData()
    }
}