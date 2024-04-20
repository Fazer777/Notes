package com.project.domain.models.task

import java.io.Serializable

data class SubtaskParam (
    var title : String,
    var isChecked : Boolean,
    var taskId : Int
) : Serializable
