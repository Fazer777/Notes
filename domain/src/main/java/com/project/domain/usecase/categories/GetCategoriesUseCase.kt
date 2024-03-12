package com.project.domain.usecase.categories

import com.project.domain.models.CategoryInterim
import com.project.domain.repository.ICategoryRepository

class GetCategoriesUseCase(private val categoryRepository: ICategoryRepository) {
    suspend fun execute() : List<CategoryInterim>{
        return categoryRepository.getCategories()
    }
}