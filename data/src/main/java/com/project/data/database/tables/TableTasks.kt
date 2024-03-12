package com.project.data.database.tables

class TableTasks {
    companion object{
        const val TABLE_NAME = "Tasks"
        const val ID = "taskId"
        const val TITLE = "taskTitle"
        const val DESCRIPTION = "taskDescription"
        const val DATE_APPOINTED = "taskDateAppointed"
        const val DATE_COMPLETION = "taskDateCompletion"
        const val COLOR = "taskColor"
        const val IS_COMPLETED = "taskIsCompleted"

        const val CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                    "$ID INTEGER PRIMARY KEY, " +
                    "$TITLE TEXT, " +
                    "$DESCRIPTION TEXT, " +
                    "$DATE_APPOINTED TEXT, " +
                    "$DATE_COMPLETION TEXT, " +
                    "$COLOR INTEGER," +
                    "$IS_COMPLETED INTEGER)"

        const val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}