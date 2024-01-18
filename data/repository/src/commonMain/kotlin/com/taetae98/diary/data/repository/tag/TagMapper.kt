package com.taetae98.diary.data.repository.tag

import com.taetae98.diary.data.dto.tag.TagDto
import com.taetae98.diary.domain.entity.tag.Tag
import kotlinx.datetime.Clock

internal fun Tag.toDto(): TagDto {
    return TagDto(
        id = id,
        title = title,
        description = description,
        isMemoTag = isMemoTag,
        isCalendarTag = isCalendarTag,
        ownerId = ownerId,
        updateAt = Clock.System.now(),
    )
}

internal fun TagDto.toDomain(): Tag {
    return Tag(
        id = id,
        title = title,
        description = description,
        isMemoTag = isMemoTag,
        isCalendarTag = isCalendarTag,
        ownerId = ownerId,
    )
}