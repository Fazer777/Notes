package com.project.data.models

import java.time.LocalDateTime

data class Note(
    val noteDescription : String,
    var category: Category,
    val noteDate: LocalDateTime,
    // This is necessary for correct deletion from the database
    val itemIndex : Int
)