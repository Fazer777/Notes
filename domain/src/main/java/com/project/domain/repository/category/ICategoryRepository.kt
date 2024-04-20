package com.project.domain.repository.category

import com.project.domain.models.category.CategoryParam
import kotlinx.coroutines.flow.Flow

interface ICategoryRepository {
    suspend fun addCategory(categoryParam: CategoryParam)
    suspend fun deleteCategory(categoryParam: CategoryParam)
    suspend fun updateNotesWithDeletedCategory(defaultCategoryId : Long, deletedCategoryId : Long)
    fun getCategories() : Flow<List<CategoryParam>>
}