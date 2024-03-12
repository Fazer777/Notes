package com.project.domain.models

import java.io.Serializable
import java.time.LocalDate

data class TaskInterim(
    var id : Int,
    val title : String,
    val description : String,
    var subTasks : List<SubTaskInterim>?,
    val color : Int,
    val appointedDate : LocalDate, // Назначеная дата пользователем при создании задачи
    var completionDate : LocalDate?, // Дата завершения задачи
    var isChecked : Boolean = false
) : Serializable