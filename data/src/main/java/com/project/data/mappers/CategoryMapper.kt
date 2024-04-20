package com.project.data.mappers

import com.project.data.entities.category.CategoryEntity
import com.project.domain.models.category.CategoryParam

internal abstract class CategoryMapper {
    companion object{
        fun mapToData(categoryParam: CategoryParam) : CategoryEntity {
            return CategoryEntity(
                id = categoryParam.id,
                name = categoryParam.name,
                color = categoryParam.color
            )
        }

        fun mapToDomain(categoryEntity: CategoryEntity) : CategoryParam {
            return CategoryParam(
                id = categoryEntity.id,
                name = categoryEntity.name,
                color = categoryEntity.color
            )
        }
    }
}