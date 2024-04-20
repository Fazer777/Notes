package com.project.data.entities.task

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    @ColumnInfo(name = "title")
    val title : String,
    @ColumnInfo(name = "description")
    val description : String,
    @ColumnInfo(name = "subtasksJson")
    var subTasksJson : String,
    @ColumnInfo(name = "color")
    val color : Int,
    @ColumnInfo(name = "appointedDate")
    val appointedDate : String, // Назначеная дата пользователем при создании задачи
    @ColumnInfo(name = "completionDate")
    var completionDate : String?, // Дата завершения задачи
    @ColumnInfo(name = "isChecked")
    var isChecked : Boolean = false
) : Serializable