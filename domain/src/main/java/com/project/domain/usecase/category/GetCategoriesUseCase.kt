package com.project.domain.usecase.category

import com.project.domain.models.category.CategoryParam
import com.project.domain.repository.category.ICategoryRepository
import kotlinx.coroutines.flow.Flow

class GetCategoriesUseCase(private val categoryRepository: ICategoryRepository) {
    fun execute() : Flow<List<CategoryParam>> {
        return categoryRepository.getCategories()
    }
}