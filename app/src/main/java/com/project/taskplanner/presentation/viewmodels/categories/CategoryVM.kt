package com.project.taskplanner.presentation.viewmodels.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.domain.models.CategoryInterim
import com.project.domain.usecase.categories.AddCategoryUseCase
import com.project.domain.usecase.categories.DeleteCategoryUseCase
import com.project.domain.usecase.categories.GetCategoriesUseCase

class CategoryVM(
    private val addCategoryUseCase: AddCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel(){

    private val categoriesMutableLive = MutableLiveData<ArrayList<CategoryInterim>>()
    val categoriesLive : LiveData<ArrayList<CategoryInterim>> = categoriesMutableLive

    init {
        categoriesMutableLive.value = getCategoriesUseCase.execute() as ArrayList<CategoryInterim>
    }

    fun onAddButtonClicked(categoryInterim: CategoryInterim){
        addCategoryUseCase.execute(categoryInterim)
    }

    fun onDeleteButtonClicked(itemIndex : Int){
        deleteCategoryUseCase.execute(itemIndex)
    }
    
}