package com.project.domain.usecase.category

import com.project.domain.repository.category.ICategoryRepository

class UpdateDeletedCategoryUseCase(private val categoryRepository : ICategoryRepository) {
    suspend fun execute(defaultCategoryId : Long, deletedCategoryId : Long){
        categoryRepository.updateNotesWithDeletedCategory(
            defaultCategoryId = defaultCategoryId,
            deletedCategoryId = deletedCategoryId
        )
    }
}