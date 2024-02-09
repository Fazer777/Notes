package com.project.domain.usecase.categories

import com.project.domain.models.CategoryInterim
import com.project.domain.repository.ICategoryRepository

class AddCategoryUseCase(private val categoryRepository : ICategoryRepository) {
    fun execute(categoryInterim: CategoryInterim){
        categoryRepository.addCategory(categoryInterim)
    }
}