package com.project.data.repository

import com.project.data.database.dao.dao_interface.INoteDao
import com.project.data.models.Category
import com.project.data.models.Note
import com.project.domain.models.CategoryInterim
import com.project.domain.models.NoteInterim
import com.project.domain.repository.INoteRepository


class NoteRepositoryImpl(val noteDao : INoteDao) : INoteRepository {

    override fun addNote(noteInterim: NoteInterim) {
        noteDao.addNote(mapToNote(noteInterim))
    }

    override  fun updateNote(noteInterim: NoteInterim) {
        noteDao.updateNote(mapToNote(noteInterim))
    }

    override  fun deleteNote(itemIndex: Int) {
        noteDao.deleteNote(itemIndex)
    }

    override fun getNotes(): List<NoteInterim> {
        return noteDao.readAllData().map { it -> mapToNoteInterim(it)}
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