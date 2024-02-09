package com.project.domain.repository

import com.project.domain.models.CategoryInterim

interface ICategoryRepository {
    fun addCategory(categoryInterim: CategoryInterim) : Unit
    fun deleteCategory(itemIndex : Int)
    fun getCategories() : List<CategoryInterim>
}