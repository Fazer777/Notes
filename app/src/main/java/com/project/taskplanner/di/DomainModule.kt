package com.project.taskplanner.di

import com.project.domain.usecase.category.AddCategoryUseCase
import com.project.domain.usecase.category.DeleteCategoryUseCase
import com.project.domain.usecase.category.GetCategoriesUseCase
import com.project.domain.usecase.category.UpdateDeletedCategoryUseCase
import com.project.domain.usecase.note.AddNoteUseCase
import com.project.domain.usecase.note.DeleteNoteUseCase
import com.project.domain.usecase.note.GetNotesUseCase
import com.project.domain.usecase.note.UpdateNoteUseCase
import com.project.domain.usecase.task.AddTaskUseCase
import com.project.domain.usecase.task.CompleteTaskUseCase
import com.project.domain.usecase.task.DeleteTaskUseCase
import com.project.domain.usecase.task.GetTaskUseCase
import com.project.domain.usecase.task.UpdateTaskUseCase
import org.koin.dsl.module

val domainModule = module {
    // Categories
    factory {
        AddCategoryUseCase(categoryRepository = get())
    }

    factory {
        DeleteCategoryUseCase(categoryRepository = get())
    }

    factory {
        GetCategoriesUseCase(categoryRepository = get())
    }

    factory{
        UpdateDeletedCategoryUseCase(get())
    }

    // Notes
    factory {
        AddNoteUseCase(noteRepository = get())
    }
    factory {
        DeleteNoteUseCase(noteRepository = get())
    }
    factory {
        GetNotesUseCase(noteRepository = get())
    }
    factory {
        UpdateNoteUseCase(noteRepository = get())
    }

    // Tasks
    factory {
        AddTaskUseCase(taskRepository = get())
    }
    factory {
        DeleteTaskUseCase(taskRepository = get())
    }
    factory {
        UpdateTaskUseCase(taskRepository = get())
    }
    factory {
        GetTaskUseCase(taskRepository = get())
    }
    factory {
        CompleteTaskUseCase(taskRepository = get())
    }
}