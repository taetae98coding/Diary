package com.taetae98.diary.data.local.impl.tag

import com.taetae98.diary.data.dto.tag.TagDto
import com.taetae98.diary.data.local.impl.TagEntity
import kotlinx.datetime.Instant

internal fun TagDto.toEntity(): TagEntity {
    return TagEntity(
        id = id,
        title = title,
        description = description,
        isMemoTag = isMemoTag,
        isCalendarTag = isCalendarTag,
        ownerId = ownerId,
        updateAt = updateAt,
    )
}

internal fun mapToTagDto(
    id: String,
    title: String,
    description: String,
    isMemoTag: Boolean,
    isCalendarTag: Boolean,
    ownerId: String?,
    updateAt: Instant,
): TagDto {
    return TagDto(
        id = id,
        title = title,
        description = description,
        isMemoTag = isMemoTag,
        isCalendarTag = isCalendarTag,
        ownerId = ownerId,
        updateAt = updateAt,
    )
}