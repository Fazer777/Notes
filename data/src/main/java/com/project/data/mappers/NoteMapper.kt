package com.project.data.mappers

import androidx.room.TypeConverter
import com.project.data.entities.note.NoteEntity
import com.project.data.entities.note.NoteTuple
import com.project.domain.models.category.CategoryParam
import com.project.domain.models.note.NoteParam
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

internal abstract class NoteMapper {
    companion object{
        @TypeConverter
        fun mapToData(noteParam: NoteParam) : NoteEntity {
            return NoteEntity(
                id = noteParam.id,
                description = noteParam.description,
                categoryId = noteParam.category.id,
                date = noteParam.date.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
            )
        }

        @TypeConverter
        fun mapToDomain(noteTuple: NoteTuple) : NoteParam {
            return NoteParam(
                id = noteTuple.id,
                description = noteTuple.description,
                category = CategoryParam(
                    id = noteTuple.categoryId,
                    name = noteTuple.categoryName,
                    color = noteTuple.categoryColor
                ),
                date = LocalDateTime.parse(noteTuple.date, DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
            )
        }
    }
}