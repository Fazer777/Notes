package com.project.data.database.dao.dao_interface

import com.project.data.models.Category

interface ICategoryDao {
    fun addCategory(category: Category) : Unit
    fun deleteCategory(itemIndex : Int)
    fun getCategories() : List<Category>
}