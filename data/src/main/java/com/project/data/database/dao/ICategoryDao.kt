package com.project.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.project.data.entities.category.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ICategoryDao {
    @Insert
    suspend fun addCategory(categoryEntity: CategoryEntity)
    @Delete
    suspend fun deleteCategory(categoryEntity: CategoryEntity)
    @Query("SELECT * FROM categories")
    fun getCategories() : Flow<List<CategoryEntity>>
    @Query("UPDATE notes SET category_id = :defaultCategoryId WHERE category_id = :deletedCategoryId")
    fun updateNotesWithDeletedCategory(defaultCategoryId: Long, deletedCategoryId: Long)
}