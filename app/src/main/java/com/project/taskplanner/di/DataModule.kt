package com.project.taskplanner.di

import com.project.data.database.NoteDatabase
import com.project.data.database.dao.ICategoryDao
import com.project.data.database.dao.INoteDao
import com.project.data.database.dao.ITaskDao
import com.project.data.repository.CategoryRepositoryImpl
import com.project.data.repository.NoteRepositoryImpl
import com.project.data.repository.TaskRepositoryImpl
import com.project.domain.repository.category.ICategoryRepository
import com.project.domain.repository.note.INoteRepository
import com.project.domain.repository.task.ITaskRepository
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val dataModule = module {

    single<NoteDatabase> {
        NoteDatabase.getInstance(get())!!
    }

    fun provideNoteDao(db : NoteDatabase) : INoteDao {
        return db.getNoteDao()
    }

    fun provideCategoryDao(db : NoteDatabase) : ICategoryDao {
        return db.getCategoryDao()
    }

    fun provideTaskDao(db : NoteDatabase) : ITaskDao {
        return db.getTaskDao()
    }

    single<INoteDao>{
        provideNoteDao(db = get())
    }

    single<ICategoryDao>{
        provideCategoryDao(db = get())
    }

    single<ITaskDao> {
        provideTaskDao(db = get())
    }

    single<ICategoryRepository> {
        CategoryRepositoryImpl(categoryDao = get(), dispatcher = Dispatchers.IO)
    }

    single<INoteRepository> {
        NoteRepositoryImpl(noteDao = get(), dispatcher = Dispatchers.IO)
    }

    single<ITaskRepository> {
        TaskRepositoryImpl(taskDao = get(), dispatcher = Dispatchers.IO)
    }
}