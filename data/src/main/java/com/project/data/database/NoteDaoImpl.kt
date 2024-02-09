package com.project.data.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.project.data.database.dao.NoteDao
import com.project.data.database.tables.TableCategories
import com.project.data.database.tables.TableNotes
import com.project.data.models.Category
import com.project.data.models.Note
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class NoteDaoImpl(val context : Context) : NoteDao {

    private val dbHelper = DbHelper(context)

    override fun addNote(note: Note): Unit {
        val r : Runnable = Runnable {
            val cv = ContentValues()
            cv.put(TableNotes.DESCRIPTION, note.noteDescription)
            cv.put(
                TableNotes.DATE_TIME,
                note.noteDate.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
            )
            cv.put(
                TableNotes.CATEGORY_ID,
                getCategoryIdByName(nameCategory = note.category.name)
            )
            cv.put(TableNotes.ITEM_INDEX, note.itemIndex)

            insert(cv)

        }
        Thread(r, "ThreadInsertNote").start()
    }

    override fun deleteNote(itemIndex: Int) {
        val r : Runnable = Runnable {
            val whereDeleteNote = "${TableNotes.ITEM_INDEX} = ?"
            val whereArgs = arrayOf(itemIndex.toString())
            dbHelper.writableDatabase.use {
                it.beginTransaction()
                try {
                    val res = it.delete(TableNotes.TABLE_NAME, whereDeleteNote, whereArgs)
                    if (res != 0){
                        it.setTransactionSuccessful()
                    }
                    it.execSQL("UPDATE ${TableNotes.TABLE_NAME} " +
                            "SET ${TableNotes.ITEM_INDEX} = ${TableNotes.ITEM_INDEX} - 1 " +
                            "WHERE ${TableNotes.ITEM_INDEX} > $itemIndex")
                } finally {
                    it.endTransaction()
                }
            }
        }
        Thread(r, "ThreadDeleteNote").start()
    }

    override fun updateNote(note: Note) {
        val r = Runnable {
            val cv = ContentValues()
            cv.put(TableNotes.DESCRIPTION, note.noteDescription)
            cv.put(TableNotes.CATEGORY_ID, getCategoryIdByName(note.category.name))
            cv.put(
                TableNotes.DATE_TIME,
                note.noteDate.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
            )
            cv.put(TableNotes.ITEM_INDEX, note.itemIndex)

            updateByItemIndex(cv)
        }

        Thread(r, "ThreadUpdateNote").start()
    }


    override fun readAllData(): MutableList<Note> {
        val listNotes = mutableListOf<Note>()

        val sqlQuery = "SELECT * " +
                "FROM " +
                "${TableNotes.TABLE_NAME}, ${TableCategories.TABLE_NAME} " +
                "WHERE " +
                "${TableNotes.TABLE_NAME}.${TableNotes.CATEGORY_ID} = ${TableCategories.TABLE_NAME}.${TableCategories.ID}"

        dbHelper.readableDatabase.use {
            it.rawQuery(sqlQuery, null).use { curs ->
                while (curs.moveToNext()) {
                    val note = Note(
                        curs.getString(curs.getColumnIndexOrThrow(TableNotes.DESCRIPTION)),
                        Category(
                            curs.getString(curs.getColumnIndexOrThrow(TableCategories.NAME)),
                            curs.getInt(curs.getColumnIndexOrThrow(TableCategories.COLOR)),
                            curs.getInt(curs.getColumnIndexOrThrow(TableCategories.ITEM_INDEX))
                        ),
                        // TODO
                        noteDate = LocalDateTime.parse(
                            curs.getString(curs.getColumnIndexOrThrow(TableNotes.DATE_TIME)),
                            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                        ),
                        curs.getInt(curs.getColumnIndexOrThrow(TableNotes.ITEM_INDEX))
                    )
                    listNotes.add(note)
                }
            }
        }
        return listNotes
    }

    private fun insert(cv : ContentValues){
        dbHelper.writableDatabase.use {
            it.beginTransaction()
            try {
                val res : Long = it.insert(TableNotes.TABLE_NAME, null, cv)
                if(res.toInt() != -1){
                    it.setTransactionSuccessful()
                }
            }
            finally {
                it.endTransaction()
            }
        }
    }

    private fun updateByItemIndex(cv : ContentValues){
        val selection = "${TableNotes.ITEM_INDEX} = ?"
        val selectionArgs = arrayOf("${cv.getAsInteger(TableNotes.ITEM_INDEX)}")
        dbHelper.writableDatabase.use {
            it.beginTransaction()
            try {
                val res = it.update(TableNotes.TABLE_NAME, cv, selection, selectionArgs)
                if (res != -1){
                    it.setTransactionSuccessful()
                }
            } finally {
                it.endTransaction()
            }
        }
    }


    private fun getCategoryIdByName(nameCategory : String) : Int{

        val sqlQuery  = "SELECT ${TableCategories.TABLE_NAME}.${TableCategories.ID} " +
                "FROM ${TableCategories.TABLE_NAME} " +
                "WHERE ${TableCategories.NAME} = ?"

        var categoryId = 1;

        dbHelper.readableDatabase.use {
            it.rawQuery(sqlQuery, arrayOf(nameCategory)).use { cursor ->
                cursor.moveToFirst()
                categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(TableCategories.ID))
                cursor.close()
            }
        }
        return categoryId
    }

}