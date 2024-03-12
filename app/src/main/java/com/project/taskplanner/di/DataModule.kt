package com.project.taskplanner.di

import com.project.data.database.dao.CategoryDaoImpl
import com.project.data.database.dao.NoteDaoImpl
import com.project.data.database.dao.SubtaskDaoImpl
import com.project.data.database.dao.TaskDaoImpl
import com.project.data.database.dao.dao_interface.ICategoryDao
import com.project.data.database.dao.dao_interface.INoteDao
import com.project.data.database.dao.dao_interface.ISubTaskDao
import com.project.data.database.dao.dao_interface.ITaskDao
import com.project.data.repository.CategoryRepositoryImpl
import com.project.data.repository.NoteRepositoryImpl
import com.project.data.repository.SubtaskRepositoryImpl
import com.project.data.repository.TaskRepositoryImpl
import com.project.domain.repository.ICategoryRepository
import com.project.domain.repository.INoteRepository
import com.project.domain.repository.ISubtaskRepository
import com.project.domain.repository.ITaskRepository
import org.koin.dsl.module

val dataModule = module {

    single<ICategoryDao> {
        CategoryDaoImpl(context = get())
    }

    single<INoteDao> {
        NoteDaoImpl(context = get())
    }

    single<ITaskDao> {
        TaskDaoImpl(context = get())
    }

    single<ISubTaskDao> {
        SubtaskDaoImpl(context = get())
    }

    single<ICategoryRepository> {
        CategoryRepositoryImpl(categoryDao = get())
    }

    single<INoteRepository>{
        NoteRepositoryImpl(noteDao = get())
    }

    single<ITaskRepository> {
        TaskRepositoryImpl(taskDao = get(), subTaskDao = get())
    }

    single<ISubtaskRepository> {
        SubtaskRepositoryImpl(subtaskDao = get())
    }
}