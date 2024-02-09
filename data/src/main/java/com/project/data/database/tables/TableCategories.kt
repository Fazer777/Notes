package com.project.data.database.tables

import android.graphics.Color

class TableCategories {

    companion object{
        const val TABLE_NAME = "Categories"
        const val ID = "categoryId"
        const val NAME = "categoryName"
        const val COLOR = "categoryColor"
        const val ITEM_INDEX = "categoryItemIndex"

        const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "$ID INTEGER PRIMARY KEY," +
                "$NAME TEXT," +
                "$COLOR INTEGER," +
                "$ITEM_INDEX INTEGER)"

        const val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"


        private const val DEFAULT_CATEGORY = "#ededf8"

        val INSERT_DEFAULT_CATEGORIES =
            "INSERT INTO $TABLE_NAME ($NAME, $COLOR, $ITEM_INDEX)" +
                    "VALUES " +
                    "('Без категории', ${Color.parseColor(DEFAULT_CATEGORY)}, 0)"
    }

}