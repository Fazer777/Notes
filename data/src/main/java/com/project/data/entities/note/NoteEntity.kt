package com.project.data.entities.note

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.project.data.entities.category.CategoryEntity

@Entity(tableName = "notes",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        )
    ])
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Long,
    @ColumnInfo(name = "description")
    val description : String,
    @ColumnInfo(name = "category_id")
    var categoryId: Long,
    @ColumnInfo(name = "date")
    val date: String
)