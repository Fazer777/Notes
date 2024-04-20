package com.project.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.project.data.entities.note.NoteEntity
import com.project.data.entities.note.NoteTuple
import kotlinx.coroutines.flow.Flow

@Dao
interface INoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNote(noteEntity : NoteEntity)
    @Delete
    suspend fun deleteNote(noteEntity: NoteEntity)
    @Update
    suspend fun updateNote(noteEntity : NoteEntity)

    @Query("SELECT notes.id, description, categories.id as cat_id, category_name, category_color, date FROM notes\n" +
            "INNER JOIN categories ON notes.category_id = categories.id" )
    fun getNotes() : Flow<List<NoteTuple>>
}