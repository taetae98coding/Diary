package io.github.taetae98coding.diary.core.diary.database.room.mapper

import io.github.taetae98coding.diary.core.diary.database.room.entity.MemoEntity
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.core.model.memo.MemoDto

internal fun MemoDto.toEntity(): MemoEntity =
    MemoEntity(
        id = id,
        title = detail.title,
        description = detail.description,
        start = detail.start,
        endInclusive = detail.endInclusive,
        color = detail.color,
        isFinish = isFinish,
        isDelete = isDelete,
        owner = owner,
        primaryTag = primaryTag,
        updateAt = updateAt,
        serverUpdateAt = serverUpdateAt,
    )

internal fun MemoEntity.toDto(): MemoDto =
    MemoDto(
        id = id,
        detail =
            MemoDetail(
                title = title,
                description = description,
                start = start,
                endInclusive = endInclusive,
                color = color,
            ),
        owner = owner,
        primaryTag = primaryTag,
        isFinish = isFinish,
        isDelete = isDelete,
        updateAt = updateAt,
        serverUpdateAt = serverUpdateAt,
    )
