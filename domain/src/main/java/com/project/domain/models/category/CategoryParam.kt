package com.project.domain.models.category

import java.io.Serializable

data class CategoryParam (
    val id : Long,
    val name : String,
    val color : Int,
) : Serializable