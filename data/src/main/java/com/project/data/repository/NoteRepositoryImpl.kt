package com.project.data.repository

import com.project.data.database.dao.INoteDao
import com.project.data.mappers.NoteMapper
import com.project.domain.models.note.NoteParam
import com.project.domain.repository.note.INoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


class NoteRepositoryImpl(
    private val noteDao : INoteDao,
    private val dispatcher: CoroutineDispatcher
) : INoteRepository {

    override suspend fun addNote(noteInterim: NoteParam) {
        withContext(dispatcher){
            noteDao.addNote(NoteMapper.mapToData(noteInterim))
        }
    }

    override suspend fun updateNote(noteInterim: NoteParam) {
        withContext(dispatcher){
            noteDao.updateNote(NoteMapper.mapToData(noteInterim))
        }
    }

    override suspend fun deleteNote(noteInterim: NoteParam) {
        withContext(dispatcher){
            noteDao.deleteNote(NoteMapper.mapToData(noteInterim))
        }
    }

    override fun getNotes(): Flow<List<NoteParam>> {
        return noteDao.getNotes().map { list -> list.map { noteTuple -> NoteMapper.mapToDomain(noteTuple) } }
    }

}