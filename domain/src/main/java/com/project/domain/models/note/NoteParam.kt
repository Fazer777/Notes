package com.project.domain.models.note


import com.project.domain.models.category.CategoryParam
import java.io.Serializable
import java.time.LocalDateTime

data class NoteParam(
    val id : Long,
    val description : String,
    val category: CategoryParam,
    val date: LocalDateTime,
) : Serializable