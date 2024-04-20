package com.project.taskplanner.presentation.viewmodels.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.project.domain.models.note.NoteParam
import com.project.domain.usecase.note.DeleteNoteUseCase
import com.project.domain.usecase.note.GetNotesUseCase
import kotlinx.coroutines.launch

class FragmentNotesVM (
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val getNotesUseCase: GetNotesUseCase

): ViewModel() {

    val notes : LiveData<List<NoteParam>> = getNotesLive()


    fun deleteNote(noteParam: NoteParam){
        viewModelScope.launch{
            deleteNoteUseCase.execute(noteParam = noteParam)
        }
    }

    private fun getNotesLive() : LiveData<List<NoteParam>>{
        return getNotesUseCase.execute().asLiveData()
    }


}