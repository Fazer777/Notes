package com.project.domain.models

import java.io.Serializable

data class SubTaskInterim (
    var id : Int,
    var title : String,
    var isChecked : Boolean,
    var taskId : Int
) : Serializable
