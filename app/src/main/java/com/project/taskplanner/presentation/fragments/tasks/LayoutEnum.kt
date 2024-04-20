package com.project.taskplanner.presentation.fragments.tasks

enum class LayoutEnum(val code : Int) {
    LINEAR(1),
    GRID(0);
    companion object {
        fun getByValue(value : Int) = entries.firstOrNull { it.code == value }
    }
}