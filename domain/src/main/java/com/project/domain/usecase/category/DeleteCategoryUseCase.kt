package com.project.domain.usecase.category

import com.project.domain.models.category.CategoryParam
import com.project.domain.repository.category.ICategoryRepository

class DeleteCategoryUseCase(private val categoryRepository: ICategoryRepository) {
    suspend fun execute(categoryParam: CategoryParam){
        categoryRepository.deleteCategory(categoryParam)
    }
}