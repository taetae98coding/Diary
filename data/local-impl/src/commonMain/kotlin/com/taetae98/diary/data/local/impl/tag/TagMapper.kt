package com.taetae98.diary.data.local.impl.tag

import com.taetae98.diary.data.dto.tag.TagDto
import com.taetae98.diary.data.local.impl.TagEntity
import kotlinx.datetime.Instant

internal fun TagDto.toEntity(): TagEntity {
    return TagEntity(
        id = id,
        title = title,
        description = description,
        ownerId = ownerId,
        updateAt = updateAt,
    )
}

internal fun mapToTagDto(
    id: String,
    title: String,
    description: String,
    ownerId: String?,
    updateAt: Instant,
): TagDto {
    return TagDto(
        id = id,
        title = title,
        description = description,
        isDeleted = false,
        ownerId = ownerId,
        updateAt = updateAt,
    )
}
