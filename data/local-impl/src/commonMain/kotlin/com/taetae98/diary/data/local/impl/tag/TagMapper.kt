package com.taetae98.diary.data.local.impl.tag

import com.taetae98.diary.data.dto.tag.TagDto
import com.taetae98.diary.data.local.impl.TagEntity

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