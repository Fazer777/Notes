package com.project.taskplanner.di

import com.project.taskplanner.presentation.viewmodels.categories.CategoryVM
import com.project.taskplanner.presentation.viewmodels.notes.AddNoteVM
import com.project.taskplanner.presentation.viewmodels.notes.FragmentNotesVM
import com.project.taskplanner.presentation.viewmodels.notes.UpdateNoteVM
import com.project.taskplanner.presentation.viewmodels.tasks.FragmentTaskVM
import com.project.taskplanner.presentation.viewmodels.tasks.subtask.SubtaskViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel<FragmentNotesVM> {
        FragmentNotesVM(
            addNoteUseCase = get(),
            updateNoteUseCase = get(),
            deleteNoteUseCase = get(),
            getNotesUseCase = get()
        )
    }

    viewModel<AddNoteVM>{
        AddNoteVM(
            getCategoriesUseCase = get()
        )
    }

    viewModel<UpdateNoteVM>{
        UpdateNoteVM(
            getCategoriesUseCase = get()
        )
    }

    viewModel<CategoryVM>{
        CategoryVM(
            addCategoryUseCase = get(),
            deleteCategoryUseCase = get(),
            getCategoriesUseCase = get()
        )
    }

    viewModel<FragmentTaskVM>{
        FragmentTaskVM(
            addTaskUseCase = get(),
            getTaskUseCase = get(),
            updateTaskUseCase = get(),
            deleteTaskUseCase = get(),
            completeTaskUseCase = get()
        )
    }

    viewModel<SubtaskViewModel>{
        SubtaskViewModel(
            addSubtaskUseCase = get(),
            getSubtaskUseCase = get()
        )
    }

}