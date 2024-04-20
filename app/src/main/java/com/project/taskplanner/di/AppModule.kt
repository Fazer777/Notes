package com.project.taskplanner.di

import com.project.taskplanner.presentation.viewmodels.categories.CategoryVM
import com.project.taskplanner.presentation.viewmodels.notes.AddNoteVM
import com.project.taskplanner.presentation.viewmodels.notes.FragmentNotesVM
import com.project.taskplanner.presentation.viewmodels.notes.UpdateNoteVM
import com.project.taskplanner.presentation.viewmodels.tasks.EditTaskViewModel
import com.project.taskplanner.presentation.viewmodels.tasks.FragmentTaskVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel<FragmentNotesVM> {
        FragmentNotesVM(
            deleteNoteUseCase = get(),
            getNotesUseCase = get()
        )
    }

    viewModel<AddNoteVM>{
        AddNoteVM(
            addNoteUseCase = get(),
            getCategoriesUseCase = get()
        )
    }

    viewModel<UpdateNoteVM>{
        UpdateNoteVM(
            updateNoteUseCase = get(),
            getCategoriesUseCase = get()
        )
    }

    viewModel<CategoryVM>{
        CategoryVM(
            addCategoryUseCase = get(),
            updateDeletedCategoryUseCase = get(),
            deleteCategoryUseCase = get(),
            getCategoriesUseCase = get()
        )
    }

    viewModel<FragmentTaskVM>{
        FragmentTaskVM(
            getTaskUseCase = get(),
            deleteTaskUseCase = get(),
            completeTaskUseCase = get()
        )
    }

    viewModel<EditTaskViewModel>{
        EditTaskViewModel(
            addTaskUseCase = get(),
            updateTaskUseCase = get()
        )
    }

}