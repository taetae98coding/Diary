package io.github.taetae98coding.diary.core.diary.service.mapper

import io.github.taetae98coding.diary.common.model.memo.LegacyMemoEntity
import io.github.taetae98coding.diary.common.model.memo.MemoEntity
import io.github.taetae98coding.diary.core.model.memo.MemoAndTagIds
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.core.model.memo.MemoDto

internal fun MemoAndTagIds.toEntity(): LegacyMemoEntity {
    return LegacyMemoEntity(
        id = memo.id,
        title = memo.detail.title,
        description = memo.detail.description,
        start = memo.detail.start,
        endInclusive = memo.detail.endInclusive,
        color = memo.detail.color,
        owner = requireNotNull(memo.owner),
        primaryTag = memo.primaryTag,
        tagIds = tagIds,
        isFinish = memo.isFinish,
        isDelete = memo.isDelete,
        updateAt = memo.updateAt,
    )
}

internal fun LegacyMemoEntity.toDto(): MemoDto {
    return MemoDto(
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
        serverUpdateAt = updateAt,
    )
}

internal fun MemoEntity.toDto(): MemoDto {
    return MemoDto(
        id = id,
        detail =
            MemoDetail(
                title = title,
                description = description,
                start = start,
                endInclusive = endInclusive,
                color = color,
            ),
        owner = null,
        primaryTag = primaryTag,
        isFinish = isFinish,
        isDelete = isDelete,
        updateAt = updateAt,
        serverUpdateAt = updateAt,
    )
}
