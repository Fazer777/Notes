package com.project.data.database.dao

import android.content.ContentValues
import android.content.Context
import com.project.data.database.DbHelper
import com.project.data.database.dao.dao_interface.ISubTaskDao
import com.project.data.database.tables.TableSubTasks
import com.project.data.models.SubTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// TODO(Rework with content provider and multiThreading)
class SubtaskDaoImpl(context: Context) : ISubTaskDao {

    private val dbHelper = DbHelper(context)
    override suspend fun addSubTask(taskId : Int, subTasks: List<SubTask>): List<Int> = withContext(Dispatchers.IO) {
        val listId = mutableListOf<Int>()

        dbHelper.writableDatabase.use { db ->
            db.beginTransaction()
            try {
                subTasks.forEach {
                    val cv = ContentValues()
                    cv.put(TableSubTasks.TITLE, it.title)
                    cv.put(TableSubTasks.IS_COMPLETED, it.isChecked.toInt())
                    cv.put(TableSubTasks.TASK_ID, taskId)

                    val insertedId = db.insert(TableSubTasks.TABLE_NAME, null, cv)
                    if (insertedId.toInt() == -1) {
                        throw Exception("Inserting row has been failed")
                    }
                    listId.add(insertedId.toInt())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                listId.clear()
                db.endTransaction()
            } finally {
                if (db.inTransaction()) {
                    db.setTransactionSuccessful()
                    db.endTransaction()
                }
            }
        }
        return@withContext listId
    }

    override suspend fun deleteSubTask(taskId: Int) = withContext(Dispatchers.IO) {

        val whereClause = "${TableSubTasks.TASK_ID} = ?"
        val whereArgs = arrayOf(taskId.toString())
        dbHelper.writableDatabase.use { db ->
            db.beginTransaction()
            try {
                db.delete(TableSubTasks.TABLE_NAME, whereClause, whereArgs)
                db.setTransactionSuccessful()
            } finally {
                db.endTransaction()
            }
        }

    }

    override suspend fun getSubTasks(): List<SubTask> = withContext(Dispatchers.IO){
        val subtaskList = mutableListOf<SubTask>()
        val sqlQuery = "SELECT * FROM ${TableSubTasks.TABLE_NAME}"

        dbHelper.readableDatabase.use { db ->
            val cursor = db.rawQuery(sqlQuery, null)
            while (cursor.moveToNext()) {
                val subTask = SubTask(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(TableSubTasks.ID)),
                    title = cursor.getString(cursor.getColumnIndexOrThrow(TableSubTasks.TITLE)),
                    isChecked = cursor.getInt(cursor.getColumnIndexOrThrow(TableSubTasks.IS_COMPLETED))
                        .toBoolean(),
                    taskId = cursor.getInt(cursor.getColumnIndexOrThrow(TableSubTasks.TASK_ID))
                )
                subtaskList.add(subTask)
            }
            cursor.close()
        }
        return@withContext subtaskList
    }

    // Function extensions
    private fun Int.toBoolean() = this == 1
    private fun Boolean.toInt() = if (this) 1 else 0
}