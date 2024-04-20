package com.project.domain.models.task

import java.io.Serializable
import java.time.LocalDate

data class TaskParam(
    var id : Int,
    val title : String,
    val description : String,
    var subTasks : List<SubtaskParam> = listOf(),
    val color : Int,
    val appointedDate : LocalDate, // Назначеная дата пользователем при создании задачи
    var completionDate : LocalDate?, // Дата завершения задачи
    var isChecked : Boolean = false
) : Serializable