package com.project.taskplanner.di

import com.project.domain.usecase.categories.AddCategoryUseCase
import com.project.domain.usecase.categories.DeleteCategoryUseCase
import com.project.domain.usecase.categories.GetCategoriesUseCase
import com.project.domain.usecase.notes.AddNoteUseCase
import com.project.domain.usecase.notes.DeleteNoteUseCase
import com.project.domain.usecase.notes.GetNotesUseCase
import com.project.domain.usecase.notes.UpdateNoteUseCase
import com.project.domain.usecase.tasks.AddTaskUseCase
import com.project.domain.usecase.tasks.CompleteTaskUseCase
import com.project.domain.usecase.tasks.DeleteTaskUseCase
import com.project.domain.usecase.tasks.GetTaskUseCase
import com.project.domain.usecase.tasks.UpdateTaskUseCase
import com.project.domain.usecase.tasks.subtask.AddSubtaskUseCase
import com.project.domain.usecase.tasks.subtask.GetSubtaskUseCase
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

    // Subtasks
    factory {
        AddSubtaskUseCase(subtaskRepository = get())
    }

    factory {
        GetSubtaskUseCase(subtaskRepository = get())
    }
}