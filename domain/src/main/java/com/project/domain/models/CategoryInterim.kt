package com.project.domain.models

import java.io.Serializable

data class CategoryInterim (
    val name : String,
    val color : Int,
    var itemIndex : Int
) : Serializable