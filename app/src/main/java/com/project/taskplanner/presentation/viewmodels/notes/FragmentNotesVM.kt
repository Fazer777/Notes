package com.project.taskplanner.presentation.viewmodels.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.domain.models.NoteInterim
import com.project.domain.usecase.notes.AddNoteUseCase
import com.project.domain.usecase.notes.DeleteNoteUseCase
import com.project.domain.usecase.notes.UpdateNoteUseCase
import com.project.domain.usecase.notes.GetNotesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentNotesVM (
    private val addNoteUseCase: AddNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val getNotesUseCase: GetNotesUseCase

): ViewModel() {

    private val notesMutableLive = MutableLiveData<ArrayList<NoteInterim>>()
    val notesLive : LiveData<ArrayList<NoteInterim>> = notesMutableLive


    suspend fun onAddNoteEvent(noteInterim: NoteInterim) {
        addNoteUseCase.execute(noteInterim)
    }

    suspend fun onUpdateNoteEvent(noteInterim: NoteInterim) {
        updateNoteUseCase.execute(noteInterim)
    }

    suspend fun onDeleteNoteEvent(itemIndex : Int) {
        deleteNoteUseCase.execute(itemIndex)
    }

    suspend fun getNotes() {
        notesMutableLive.postValue(getNotesUseCase.execute())
    }


}