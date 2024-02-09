package com.project.taskplanner.presentation.viewmodels.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.domain.models.NoteInterim
import com.project.domain.usecase.notes.AddNoteUseCase
import com.project.domain.usecase.notes.DeleteNoteUseCase
import com.project.domain.usecase.notes.UpdateNoteUseCase
import com.project.domain.usecase.notes.GetNotesUseCase

class FragmentNotesVM (
    private val addNoteUseCase: AddNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val getNotesUseCase: GetNotesUseCase

): ViewModel() {

    private val notesMutableLive = MutableLiveData<ArrayList<NoteInterim>>()
    val notesLive : LiveData<ArrayList<NoteInterim>> = notesMutableLive

    init{
        getNotes()
    }

    fun onAddNoteEvent(noteInterim: NoteInterim): Unit {
        addNoteUseCase.execute(noteInterim)
    }

    fun onUpdateNoteEvent(noteInterim: NoteInterim): Unit {
        updateNoteUseCase.execute(noteInterim)
    }

    fun onDeleteNoteEvent(itemIndex : Int): Unit {
        deleteNoteUseCase.execute(itemIndex)
    }

    fun getNotes() {
        notesMutableLive.value = getNotesUseCase.execute()
    }


}