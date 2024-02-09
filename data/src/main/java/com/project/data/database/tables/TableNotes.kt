package com.project.data.database.tables

class TableNotes {

    companion object{
        const val TABLE_NAME = "Notes"
        const val ID  = "noteId"
        const val DESCRIPTION = "noteDescription"
        const val DATE_TIME = "noteDateTime"
        const val CATEGORY_ID = "categoryId"
        const val ITEM_INDEX = "noteItemIndex"

        const val CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                    "$ID INTEGER PRIMARY KEY," +
                    "$DESCRIPTION TEXT," +
                    "$DATE_TIME TEXT," +
                    "$CATEGORY_ID INTEGER," +
                    "$ITEM_INDEX INTEGER," +
                    "FOREIGN KEY ($CATEGORY_ID) REFERENCES" +
                    " ${TableCategories.TABLE_NAME} (${TableCategories.ID}))"

        const val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}