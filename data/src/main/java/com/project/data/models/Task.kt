package com.project.data.models

import com.project.domain.models.SubTaskInterim
import java.io.Serializable
import java.time.LocalDate

data class Task(
    val id : Int,
    val title : String,
    val description : String,
    var subTasks : List<SubTask>?,
    val color : Int,
    val appointedDate : LocalDate, // Назначеная дата пользователем при создании задачи
    var completionDate : LocalDate?, // Дата завершения задачи
    var isChecked : Boolean = false
) : Serializable