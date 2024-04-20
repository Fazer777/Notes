package com.project.data.entities.category

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity (
    @PrimaryKey(autoGenerate = true)
    val id : Long,
    @ColumnInfo(name = "category_name")
    val name : String,
    @ColumnInfo(name ="category_color")
    val color : Int
)