package com.project.taskplanner.presentation.viewmodels.categories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.data.database.dao.CategoryDaoImpl
import com.project.data.repository.CategoryRepositoryImpl
import com.project.domain.usecase.categories.AddCategoryUseCase
import com.project.domain.usecase.categories.DeleteCategoryUseCase
import com.project.domain.usecase.categories.GetCategoriesUseCase

class CategoryViewModelFactory(context : Context) : ViewModelProvider.Factory{

    private val categoryRepository by lazy {
        CategoryRepositoryImpl(categoryDao = CategoryDaoImpl(context))
    }

    private val addCategoryUseCase by lazy {
        AddCategoryUseCase(categoryRepository = categoryRepository)
    }

    private val deleteCategoryUseCase by lazy{
        DeleteCategoryUseCase(categoryRepository = categoryRepository)
    }

    private val getCategoriesUseCase by lazy {
        GetCategoriesUseCase(categoryRepository = categoryRepository)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryVM(
            addCategoryUseCase,
            deleteCategoryUseCase,
            getCategoriesUseCase) as T
    }
}