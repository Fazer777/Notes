package com.project.data.repository

import com.project.data.database.dao.dao_interface.INoteDao
import com.project.data.models.Category
import com.project.data.models.Note
import com.project.domain.models.CategoryInterim
import com.project.domain.models.NoteInterim
import com.project.domain.repository.INoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class NoteRepositoryImpl(val noteDao : INoteDao) : INoteRepository {

    override suspend fun addNote(noteInterim: NoteInterim) = withContext(Dispatchers.IO) {
        noteDao.addNote(mapToNote(noteInterim))
    }

    override suspend fun updateNote(noteInterim: NoteInterim )  = withContext(Dispatchers.IO) {
        noteDao.updateNote(mapToNote(noteInterim))
    }

    override suspend fun deleteNote(itemIndex: Int)  = withContext(Dispatchers.IO){
        noteDao.deleteNote(itemIndex)
    }

    override suspend fun getNotes(): List<NoteInterim> = withContext(Dispatchers.IO)  {
        return@withContext noteDao.readAllData().map { it -> mapToNoteInterim(it)}
    }


    private fun mapToNote(noteInterim: NoteInterim) : Note {

        return Note(
            noteInterim.noteDescription,
            mapToCategory(noteInterim.category),
            noteInterim.noteDate,
            noteInterim.itemIndex
        )
    }

    private fun mapToCategory(categoryInterim: CategoryInterim) : Category{
        return Category(
            categoryInterim.name,
            categoryInterim.color,
            categoryInterim.itemIndex
        )
    }

    private fun mapToCategoryInterim(category: Category) : CategoryInterim{
        return CategoryInterim(
            category.name,
            category.color,
            category.itemIndex
        )
    }

    private fun mapToNoteInterim(note : Note) : NoteInterim {

        return NoteInterim(
            note.noteDescription,
            mapToCategoryInterim(note.category),
            note.noteDate,
            note.itemIndex
        )
    }

}