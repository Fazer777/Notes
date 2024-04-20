package com.project.data.entities.task

import java.io.Serializable

data class SubTask (
    val title : String,
    var isChecked : Boolean,
    var taskId : Int
) : Serializable