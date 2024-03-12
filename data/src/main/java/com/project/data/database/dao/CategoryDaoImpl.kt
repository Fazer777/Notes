package com.project.data.database.dao

import android.content.ContentValues
import android.content.Context
import com.project.data.database.DbHelper
import com.project.data.database.dao.dao_interface.ICategoryDao
import com.project.data.database.tables.TableCategories
import com.project.data.database.tables.TableNotes
import com.project.data.models.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.Int
import kotlin.Long
import kotlin.arrayOf


class CategoryDaoImpl(val context: Context) : ICategoryDao {

    private val dbHelper = DbHelper(context)

    override suspend fun addCategory(category: Category) = withContext(Dispatchers.IO) {

        val cv = ContentValues()
        cv.put(TableCategories.NAME, category.name)
        cv.put(TableCategories.COLOR, category.color)
        cv.put(TableCategories.ITEM_INDEX, category.itemIndex)

        insert(cv)
    }

    override suspend fun deleteCategory(itemIndex: Int) = withContext(Dispatchers.IO) {

        val sqlQuery = "SELECT ${TableCategories.TABLE_NAME}.${TableCategories.ID} " +
                "FROM ${TableCategories.TABLE_NAME} " +
                "WHERE ${TableCategories.ITEM_INDEX} = ?"

        var categoryId: Int = 1
        dbHelper.readableDatabase.use { db ->
            db.rawQuery(sqlQuery, arrayOf(itemIndex.toString())).use { cursor ->
                cursor.moveToFirst()
                categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(TableCategories.ID))
            }
        }

        // Replace category will deleted by default
        val cv = ContentValues()
        cv.put(TableNotes.CATEGORY_ID, 1)
        val whereClauseNote = "${TableNotes.CATEGORY_ID} = ?"
        val whereArgsNote = arrayOf(categoryId.toString())
        dbHelper.writableDatabase.use { db ->
            db.update(TableNotes.TABLE_NAME, cv, whereClauseNote, whereArgsNote)
        }

        // Delete category
        val whereClauseCategory = "${TableCategories.ITEM_INDEX} = ?"
        val whereArgsCategory = arrayOf(itemIndex.toString())
        dbHelper.writableDatabase.use { db ->
            db.delete(
                TableCategories.TABLE_NAME,
                whereClauseCategory,
                whereArgsCategory
            )
            db.execSQL(
                "UPDATE ${TableCategories.TABLE_NAME} " +
                        "SET ${TableCategories.ITEM_INDEX} = ${TableCategories.ITEM_INDEX} - 1 " +
                        "WHERE ${TableCategories.ITEM_INDEX} > $itemIndex"
            )
        }
    }

    override suspend fun getCategories(): List<Category> = withContext(Dispatchers.IO) {

        val listCategories = mutableListOf<Category>()

        val sqlQuery = "SELECT * FROM ${TableCategories.TABLE_NAME}"

        dbHelper.readableDatabase.use {
            it.rawQuery(sqlQuery, null).use { curs ->
                while (curs.moveToNext()) {
                    val category = Category(
                        curs.getString(curs.getColumnIndexOrThrow(TableCategories.NAME)),
                        curs.getInt(curs.getColumnIndexOrThrow(TableCategories.COLOR)),
                        curs.getInt(curs.getColumnIndexOrThrow(TableCategories.ITEM_INDEX))
                    )
                    listCategories.add(category)
                }
            }
        }
        return@withContext listCategories
    }

    private fun insert(cv: ContentValues) {
        dbHelper.writableDatabase.use {
            it.beginTransaction()
            try {
                val res: Long = it.insert(TableCategories.TABLE_NAME, null, cv)
                if (res.toInt() != -1) {
                    it.setTransactionSuccessful()
                }
            } finally {
                it.endTransaction()
            }
        }
    }
}
