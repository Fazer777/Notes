package com.project.data.entities.note

import androidx.room.ColumnInfo
import java.time.LocalDateTime

data class NoteTuple(
    val id : Long,
    @ColumnInfo(name = "description")
    val description : String,
    @ColumnInfo(name = "cat_id")
    val categoryId : Long,
    @ColumnInfo(name = "category_name")
    val categoryName: String,
    @ColumnInfo(name = "category_color")
    val categoryColor : Int,
    @ColumnInfo(name = "date")
    val date: String
)