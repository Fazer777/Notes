package com.project.data.database.tables

class TableSubTasks {
    companion object{
        const val TABLE_NAME = "SubTasks"
        const val ID = "subTaskId"
        const val TITLE = "subTaskTitle"
        const val IS_COMPLETED = "subTaskIsCompleted"
        const val TASK_ID = "taskId"

        const val CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                    "$ID INTEGER PRIMARY KEY, " +
                    "$TITLE TEXT," +
                    "$IS_COMPLETED INTEGER," +
                    "$TASK_ID INTEGER," +
                    "FOREIGN KEY ($TASK_ID) REFERENCES" +
                    " ${TableTasks.TABLE_NAME} (${TableTasks.ID}))"

        const val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}
