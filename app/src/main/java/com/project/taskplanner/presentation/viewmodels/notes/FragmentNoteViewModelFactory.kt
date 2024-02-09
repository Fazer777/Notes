package com.project.taskplanner.presentation.viewmodels.notes

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.data.database.NoteDaoImpl
import com.project.data.repository.NoteRepositoryImpl
import com.project.domain.usecase.notes.AddNoteUseCase
import com.project.domain.usecase.notes.DeleteNoteUseCase
import com.project.domain.usecase.notes.UpdateNoteUseCase
import com.project.domain.usecase.notes.GetNotesUseCase

// We can use dependencies injection instead of
class FragmentNoteViewModelFactory(context: Context): ViewModelProvider.Factory {

    private val noteRepository by lazy {
        NoteRepositoryImpl(noteDao =  NoteDaoImpl(context))
    }

    private val addNoteUseCase by lazy {
        AddNoteUseCase(noteRepository = noteRepository)
    }

    private val updateNoteUseCase by lazy {
        UpdateNoteUseCase(noteRepository =noteRepository)
    }

    private val deleteNoteUseCase by lazy{
        DeleteNoteUseCase(noteRepository =noteRepository)
    }

    private val getNotesUseCase by lazy {
        GetNotesUseCase(noteRepository = noteRepository)
    }

    @Override
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FragmentNotesVM(
            addNoteUseCase =  addNoteUseCase,
            updateNoteUseCase = updateNoteUseCase,
            deleteNoteUseCase = deleteNoteUseCase,
            getNotesUseCase = getNotesUseCase) as T
    }
}