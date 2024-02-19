package com.project.data.repository

import com.project.data.database.dao.dao_interface.ICategoryDao
import com.project.data.models.Category
import com.project.domain.models.CategoryInterim
import com.project.domain.repository.ICategoryRepository

class CategoryRepositoryImpl(val categoryDao: ICategoryDao) : ICategoryRepository {
    override fun addCategory(categoryInterim: CategoryInterim) {
        categoryDao.addCategory(mapToCategory(categoryInterim))
    }

    override fun deleteCategory(itemIndex: Int) {
        categoryDao.deleteCategory(itemIndex)
    }

    override fun getCategories(): List<CategoryInterim> {
        return categoryDao.getCategories().map { cat ->
            CategoryInterim(cat.name, cat.color, cat.itemIndex)
        }
    }

    private fun mapToCategory(categoryInterim: CategoryInterim) : Category
    {
        return Category(
            name = categoryInterim.name,
            color = categoryInterim.color,
            itemIndex = categoryInterim.itemIndex
        )
    }
}