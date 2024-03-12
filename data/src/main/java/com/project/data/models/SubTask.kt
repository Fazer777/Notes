package com.project.data.models

import java.io.Serializable

data class SubTask (
    val id : Int,
    val title : String,
    var isChecked : Boolean,
    var taskId : Int
) : Serializable