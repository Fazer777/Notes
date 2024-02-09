package com.project.data.database.dao

import com.project.data.models.Category

interface CategoryDao {
    fun addCategory(category: Category) : Unit
    fun deleteCategory(itemIndex : Int)
    fun getCategories() : List<Category>
}