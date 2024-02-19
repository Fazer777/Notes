package com.project.domain.dto

data class TaskDTO(
    val taskId : Int,
    val subtaskIdList: List<Int>?
)