package io.github.taetae98coding.diary.core.mapper

import io.github.taetae98coding.diary.core.database.api.entity.TagDetailLocalEntity
import io.github.taetae98coding.diary.core.model.tag.TagDetail

public fun TagDetail.toLocal(): TagDetailLocalEntity {
    return TagDetailLocalEntity(
        title = title,
        description = description,
        color = color,
    )
}

public fun TagDetailLocalEntity.toDomain(): TagDetail {
    return TagDetail(
        title = title,
        description = description,
        color = color,
    )
}
