package com.project.data.database.dao.dao_interface

import com.project.data.models.Category

interface ICategoryDao {
    suspend fun addCategory(category: Category) : Unit
    suspend fun deleteCategory(itemIndex : Int)
    suspend fun getCategories() : List<Category>
}