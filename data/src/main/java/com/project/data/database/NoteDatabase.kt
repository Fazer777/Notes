package com.project.data.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.project.data.database.dao.ICategoryDao
import com.project.data.database.dao.INoteDao
import com.project.data.database.dao.ITaskDao
import com.project.data.entities.category.CategoryEntity
import com.project.data.entities.note.NoteEntity
import com.project.data.entities.task.TaskEntity

const val DATABASE_VERSION_CODE = 1

@Database(entities = [NoteEntity::class, CategoryEntity::class, TaskEntity::class], version = DATABASE_VERSION_CODE)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun getNoteDao(): INoteDao
    abstract fun getCategoryDao(): ICategoryDao
    abstract fun getTaskDao(): ITaskDao

    companion object {
        private var instance: NoteDatabase? = null

        fun getInstance(context: Context): NoteDatabase? {
            if (instance == null) {
                synchronized(NoteDatabase::class) {
                    instance = Room
                        .databaseBuilder(
                            context = context,
                            klass = NoteDatabase::class.java,
                            name = "notes_database"
                        )
                        .addCallback(object : Callback() {
                            override fun onOpen(db: SupportSQLiteDatabase) {
                                super.onOpen(db)
                                for (defaultCat in defaultCategories) {
                                    val values = ContentValues().also { values ->
                                        values.put("id", defaultCat.id)
                                        values.put("category_name", defaultCat.name)
                                        values.put("category_color", defaultCat.color)
                                    }
                                    db.insert("categories", SQLiteDatabase.CONFLICT_IGNORE, values)
                                }
                            }
                        })
                        .build()
                }
            }
            return instance
        }

        private val defaultCategories = listOf<CategoryEntity>(
            CategoryEntity(
                id = 1,
                name = "Без категории",
                color = Color.parseColor("#858585")
            )
        )
    }
}