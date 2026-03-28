package io.github.taetae98coding.diary.core.mapper

import io.github.taetae98coding.diary.core.database.api.entity.MemoLocalEntity
import io.github.taetae98coding.diary.core.model.memo.Memo

public fun MemoLocalEntity.toDomain(): Memo {
    return Memo(
        id = id,
        detail = detail.toDomain(),
        primaryTag = primaryTag,
        isFinished = isFinished,
        isDeleted = isDeleted,
        updatedAt = updatedAt,
        createdAt = createdAt,
    )
}
