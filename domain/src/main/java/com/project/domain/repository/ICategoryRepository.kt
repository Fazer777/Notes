package com.project.domain.repository

import com.project.domain.models.CategoryInterim

interface ICategoryRepository {
    suspend fun addCategory(categoryInterim: CategoryInterim)
    suspend fun deleteCategory(itemIndex : Int)
    suspend fun getCategories() : List<CategoryInterim>
}