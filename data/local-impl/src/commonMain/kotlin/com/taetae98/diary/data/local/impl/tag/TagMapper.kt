package com.taetae98.diary.data.local.impl.tag

import com.taetae98.diary.data.dto.tag.TagDto
import com.taetae98.diary.data.dto.tag.TagStateDto
import com.taetae98.diary.data.local.impl.TagEntity
import kotlinx.datetime.Instant

internal fun TagStateDto.toEntity(): TagStateEntity {
    return when (this) {
        TagStateDto.NONE -> TagStateEntity.NONE
        TagStateDto.DELETE -> TagStateEntity.DELETE
    }
}

internal fun TagDto.toEntity(): TagEntity {
    return TagEntity(
        id = id,
        title = title,
        description = description,
        state = state.toEntity(),
        ownerId = ownerId,
        updateAt = updateAt,
    )
}

internal fun mapToTagDto(
    id: String,
    title: String,
    description: String,
    state: TagStateEntity,
    ownerId: String?,
    updateAt: Instant,
): TagDto {
    return TagDto(
        id = id,
        title = title,
        description = description,
        state = state.toDto(),
        ownerId = ownerId,
        updateAt = updateAt,
    )
}

internal fun TagStateEntity.toDto(): TagStateDto {
    return when (this) {
        TagStateEntity.NONE -> TagStateDto.NONE
        TagStateEntity.DELETE -> TagStateDto.DELETE
    }
}
