package com.project.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.project.data.entities.task.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ITaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(taskEntity: TaskEntity)

    @Update
    suspend fun updateTask(taskEntity: TaskEntity)

    @Query(
        "UPDATE tasks " +
                "SET isChecked = :checked, " +
                "completionDate = :completionDate " +
                "WHERE id = :taskId"
    )
    suspend fun updateTaskChecked(taskId: Int, checked: Boolean, completionDate: String?)

    @Delete
    suspend fun deleteTask(taskEntity: TaskEntity)

    @Query("SELECT * FROM tasks")
    fun getTasks(): Flow<List<TaskEntity>>

}