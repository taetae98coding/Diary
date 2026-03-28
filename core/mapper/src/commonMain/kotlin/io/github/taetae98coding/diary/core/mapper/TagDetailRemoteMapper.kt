package io.github.taetae98coding.diary.core.mapper

import io.github.taetae98coding.diary.core.database.api.entity.TagDetailLocalEntity
import io.github.taetae98coding.diary.core.network.api.entity.TagDetailRemoteEntity

public fun TagDetailLocalEntity.toRemote(): TagDetailRemoteEntity {
    return TagDetailRemoteEntity(
        title = title,
        description = description,
        color = color,
    )
}

public fun TagDetailRemoteEntity.toLocal(): TagDetailLocalEntity {
    return TagDetailLocalEntity(
        title = title,
        description = description,
        color = color,
    )
}
