package com.project.taskplanner.presentation.viewmodels.notes

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.data.database.dao.CategoryDaoImpl
import com.project.data.repository.CategoryRepositoryImpl
import com.project.domain.usecase.categories.GetCategoriesUseCase

class UpdateNoteViewModelFactory(context : Context) : ViewModelProvider.Factory  {

    private val categoryRepository by lazy {
        CategoryRepositoryImpl(categoryDao = CategoryDaoImpl(context))
    }

    private val getCategoriesUseCase by lazy {
        GetCategoriesUseCase(categoryRepository = categoryRepository)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UpdateNoteVM(getCategoriesUseCase) as T
    }
}