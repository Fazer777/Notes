package com.project.data.mappers

import com.project.data.entities.task.TaskEntity
import com.project.domain.models.task.SubtaskParam
import com.project.domain.models.task.TaskParam
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

abstract class TaskMapper {
    companion object{
        fun mapToData(taskParam: TaskParam) : TaskEntity {
            return TaskEntity(
                id = taskParam.id,
                title = taskParam.title,
                description = taskParam.description,
                subTasksJson = JsonMapper.mapListToJson(taskParam.subTasks),
                color = taskParam.color,
                appointedDate = taskParam.appointedDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)),
                completionDate = taskParam.completionDate?.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)),
                isChecked = taskParam.isChecked
            )
        }

        fun mapToDomain(taskEntity: TaskEntity) : TaskParam {
            return TaskParam(
                id = taskEntity.id,
                title = taskEntity.title,
                description = taskEntity.description,
                subTasks = JsonMapper.mapFromJsonArray(taskEntity.subTasksJson, SubtaskParam::class.java),
                color = taskEntity.color,
                appointedDate = LocalDate.parse(taskEntity.appointedDate, DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)),
                completionDate = taskEntity.completionDate?.let { LocalDate.parse(taskEntity.completionDate, DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))},
                isChecked = taskEntity.isChecked
            )
        }
    }
}