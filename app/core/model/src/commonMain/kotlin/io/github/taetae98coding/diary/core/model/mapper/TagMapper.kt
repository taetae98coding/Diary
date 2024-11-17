package io.github.taetae98coding.diary.core.model.mapper

import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.core.model.tag.TagDto

public fun Tag.toDto(): TagDto {
    return TagDto(
        id = id,
        detail = detail,
        owner = owner,
        isFinish = isFinish,
        isDelete = isDelete,
        updateAt = updateAt,
        serverUpdateAt = null,
    )
}

public fun TagDto.toTag(): Tag {
    return Tag(
        id = id,
        detail = detail,
        owner = owner,
        isFinish = isFinish,
        isDelete = isDelete,
        updateAt = updateAt,
    )
}
