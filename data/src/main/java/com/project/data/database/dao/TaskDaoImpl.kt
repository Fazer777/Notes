package com.project.data.database.dao

import android.content.ContentValues
import android.content.Context
import com.project.data.database.DbHelper
import com.project.data.database.dao.dao_interface.ITaskDao
import com.project.data.database.tables.TableTasks
import com.project.data.models.Task
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.concurrent.Exchanger

// TODO(Rework multiThreading)
class TaskDaoImpl(context : Context) : ITaskDao {

    private val dbHelper = DbHelper(context)
    override fun addTask(task: Task) : Int {
        val cv = ContentValues()
        cv.put(TableTasks.TITLE, task.title)
        cv.put(TableTasks.DESCRIPTION, task.description)
        cv.put(TableTasks.COLOR, task.color)
        cv.put(
            TableTasks.DATE_APPOINTED,
            task.appointedDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)))
        cv.put(
            TableTasks.DATE_COMPLETION,
            task.completionDate?.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
        )
        cv.put(TableTasks.IS_COMPLETED, task.isChecked.toInt())

        var insertedId : Long = 0
        dbHelper.writableDatabase.use { db->
            db.beginTransaction()
            try {
                insertedId = db.insert(TableTasks.TABLE_NAME, null, cv)
                if (insertedId.toInt() != -1){
                    db.setTransactionSuccessful()
                }
            }finally {
                db.endTransaction()
            }
        }

        return insertedId.toInt()
    }

    override fun updateTask(id: Int, task: Task) {
        TODO("Not yet implemented")
    }

    override fun deleteTask(id: Int) {
        val r = Runnable {
            val whereClause = "${TableTasks.ID} = ?"
            val whereArgs = arrayOf(id.toString())
            dbHelper.writableDatabase.use { db ->
                db.beginTransaction()
                try {
                    db.delete(TableTasks.TABLE_NAME, whereClause, whereArgs)
                    db.setTransactionSuccessful()
                }
                finally {
                    db.endTransaction()
                }
            }
        }
        Thread(r, "ThreadDeleteTask").start()

    }

    override fun getTasks(): List<Task> {
        val exchanger = Exchanger<List<Task>>()
        var taskList = listOf<Task>()
        val r = Runnable {
            val tasks = mutableListOf<Task>()
            val sqlQuery = "SELECT * FROM ${TableTasks.TABLE_NAME}"
            val exchangerThread = exchanger

            dbHelper.readableDatabase.use { db ->

                val cursor = db.rawQuery(sqlQuery, null)

                while (cursor.moveToNext()) {
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

                    tasks.add(task)
                }
                cursor.close()
            }

            try {
                exchanger.exchange(tasks)
            } catch (e: InterruptedException) {
            }
        }

        Thread(r, "ThreadGetTasks").start()

        try {
            taskList = exchanger.exchange(taskList)
        } catch (e: InterruptedException) {
            taskList = emptyList()
        }


        return taskList
    }
}

private fun Int.toBoolean(): Boolean = this == 1

private fun Boolean.toInt(): Int = if (this) 1 else 0
