package com.project.data.repository

import com.project.data.database.dao.dao_interface.ICategoryDao
import com.project.data.models.Category
import com.project.domain.models.CategoryInterim
import com.project.domain.repository.ICategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoryRepositoryImpl(val categoryDao: ICategoryDao) : ICategoryRepository {
    override suspend fun addCategory(categoryInterim: CategoryInterim) = withContext(Dispatchers.IO){
        categoryDao.addCategory(mapToCategory(categoryInterim))
    }

    override suspend fun deleteCategory(itemIndex: Int) = withContext(Dispatchers.IO){
        categoryDao.deleteCategory(itemIndex)
    }

    override suspend fun getCategories(): List<CategoryInterim> = withContext(Dispatchers.IO) {
        return@withContext categoryDao.getCategories().map { cat ->
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