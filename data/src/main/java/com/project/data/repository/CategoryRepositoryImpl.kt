package com.project.data.repository

import com.project.data.database.dao.ICategoryDao
import com.project.data.mappers.CategoryMapper
import com.project.domain.models.category.CategoryParam
import com.project.domain.repository.category.ICategoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CategoryRepositoryImpl(
    private val categoryDao: ICategoryDao,
    private val dispatcher: CoroutineDispatcher
) : ICategoryRepository {
    override suspend fun addCategory(categoryParam: CategoryParam) {
        withContext(dispatcher) {
            categoryDao.addCategory(CategoryMapper.mapToData(categoryParam))
        }
    }

    override suspend fun deleteCategory(categoryParam: CategoryParam) {
        withContext(dispatcher) {
            categoryDao.deleteCategory(CategoryMapper.mapToData(categoryParam))
        }
    }

    override suspend fun updateNotesWithDeletedCategory(
        defaultCategoryId: Long,
        deletedCategoryId: Long
    ) {
        withContext(dispatcher){
            categoryDao.updateNotesWithDeletedCategory(
                defaultCategoryId = defaultCategoryId,
                deletedCategoryId = deletedCategoryId
            )
        }
    }

    override fun getCategories(): Flow<List<CategoryParam>> {
        return categoryDao.getCategories().map { list ->
            list.map { catEntity ->
                CategoryMapper.mapToDomain(catEntity)
            }
        }
    }

}