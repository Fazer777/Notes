package com.project.taskplanner.presentation.viewmodels.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.domain.models.CategoryInterim
import com.project.domain.usecase.categories.AddCategoryUseCase
import com.project.domain.usecase.categories.DeleteCategoryUseCase
import com.project.domain.usecase.categories.GetCategoriesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryVM(
    private val addCategoryUseCase: AddCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel(){

    private val categoriesMutableLive = MutableLiveData<ArrayList<CategoryInterim>>()
    val categoriesLive : LiveData<ArrayList<CategoryInterim>> = categoriesMutableLive

    suspend fun onAddButtonClicked(categoryInterim: CategoryInterim)  {
        addCategoryUseCase.execute(categoryInterim)
    }

    suspend fun onDeleteButtonClicked(itemIndex : Int) {
        deleteCategoryUseCase.execute(itemIndex)
    }

    suspend fun getCategories(){
        categoriesMutableLive.postValue(getCategoriesUseCase.execute() as ArrayList<CategoryInterim>)
    }
}