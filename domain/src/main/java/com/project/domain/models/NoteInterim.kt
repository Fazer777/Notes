package com.project.domain.models


import java.io.Serializable
import java.time.LocalDateTime

data class NoteInterim(
    val noteDescription : String,
    val category: CategoryInterim,
    val noteDate: LocalDateTime,
    var itemIndex : Int = 0
) : Serializable