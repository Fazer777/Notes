package com.project.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.project.data.database.tables.TableCategories
import com.project.data.database.tables.TableNotes
import com.project.data.database.tables.TableSubTasks
import com.project.data.database.tables.TableTasks

internal class DbHelper(context : Context)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME : String= "TaskPlanner.db"
        private const val DATABASE_VERSION : Int = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(TableCategories.CREATE_TABLE)
        db?.execSQL(TableNotes.CREATE_TABLE)
        db?.execSQL(TableCategories.INSERT_DEFAULT_CATEGORIES)
        db?.execSQL(TableTasks.CREATE_TABLE)
        db?.execSQL(TableSubTasks.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(TableNotes.DROP_TABLE)
        db?.execSQL(TableCategories.DROP_TABLE)
        db?.execSQL(TableTasks.DROP_TABLE)
        db?.execSQL(TableSubTasks.DROP_TABLE)
        onCreate(db)
    }
}