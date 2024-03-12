package com.project.data.database.dao

import android.content.ContentValues
import android.content.Context
import com.project.data.database.DbHelper
import com.project.data.database.dao.dao_interface.ITaskDao
import com.project.data.database.tables.TableTasks
import com.project.data.models.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class TaskDaoImpl(context: Context) : ITaskDao {

    private val dbHelper = DbHelper(context)
    override suspend fun addTask(task: Task): Int = withContext(Dispatchers.IO){
        val values = ContentValues()
        values.put(TableTasks.TITLE, task.title)
        values.put(TableTasks.DESCRIPTION, task.description)
        values.put(TableTasks.COLOR, task.color)
        values.put(
            TableTasks.DATE_APPOINTED,
            task.appointedDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
        )
        values.put(
            TableTasks.DATE_COMPLETION,
            task.completionDate?.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
        )
        values.put(TableTasks.IS_COMPLETED, task.isChecked.toInt())

        var insertedId: Long = 0
        dbHelper.readableDatabase.use {db->
            insertedId = db.insert(TableTasks.TABLE_NAME,null, values)
        }
        return@withContext insertedId.toInt()
    }

    override suspend fun updateTask(taskId: Int, task: Task) : Unit = withContext(Dispatchers.IO) {

        val where = "${TableTasks.ID} = $taskId"

        val cv = ContentValues()
        cv.put(TableTasks.TITLE, task.title)
        cv.put(TableTasks.DESCRIPTION, task.description)
        cv.put(TableTasks.COLOR, task.color)
        cv.put(
            TableTasks.DATE_APPOINTED,
            task.appointedDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
        )
        cv.put(
            TableTasks.DATE_COMPLETION,
            task.completionDate?.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
        )
        cv.put(TableTasks.IS_COMPLETED, task.isChecked.toInt())

        dbHelper.readableDatabase.use { db->
            db.update(TableTasks.TABLE_NAME,cv,where, null)
        }
    }

    override suspend fun updateTaskChecked(taskId: Int, checked: Boolean, completionDate: LocalDate) : Unit = withContext(Dispatchers.IO) {
        val where = "${TableTasks.ID} = $taskId"

        val cv = ContentValues()
        cv.put(TableTasks.IS_COMPLETED, checked.toInt())
        cv.put(
            TableTasks.DATE_COMPLETION,
            completionDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
        )

        dbHelper.readableDatabase.use { db->
            db.update(TableTasks.TABLE_NAME,cv,where, null)
        }
    }

    override suspend fun deleteTask(taskId: Int) = withContext(Dispatchers.IO) {
        val whereClause = "${TableTasks.ID} = ?"
        val whereArgs = arrayOf(taskId.toString())
        dbHelper.writableDatabase.use { db ->
            db.beginTransaction()
            try {
                db.delete(TableTasks.TABLE_NAME, whereClause, whereArgs)
                db.setTransactionSuccessful()
            } finally {
                db.endTransaction()
            }
        }

    }

    override suspend fun getTasks(): List<Task> = withContext(Dispatchers.IO)  {
        val taskList = mutableListOf<Task>()

        dbHelper.readableDatabase.use { db->
            val cursor = db.query(
                TableTasks.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null)

            while (cursor.moveToNext()){
                val task = Task(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(TableTasks.ID)),
                    title = cursor.getString(cursor.getColumnIndexOrThrow(TableTasks.TITLE)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(TableTasks.DESCRIPTION)),
                    subTasks = null,
                    color = cursor.getInt(cursor.getColumnIndexOrThrow(TableTasks.COLOR)),
                    appointedDate = LocalDate.parse(
                        cursor.getString(cursor.getColumnIndexOrThrow(TableTasks.DATE_APPOINTED)),
                        DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                    ),
                    completionDate = null,
                    isChecked = cursor.getInt(cursor.getColumnIndexOrThrow(TableTasks.IS_COMPLETED))
                        .toBoolean()
                )

                val strDate =
                    cursor.getString(cursor.getColumnIndexOrThrow(TableTasks.DATE_COMPLETION))
                if (strDate != null) {
                    task.completionDate = LocalDate.parse(
                        strDate,
                        DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                    )
                }
                taskList.add(task)
            }
            cursor.close()
        }
        return@withContext taskList
    }
}

private fun Int.toBoolean(): Boolean = this == 1

private fun Boolean.toInt(): Int = if (this) 1 else 0
