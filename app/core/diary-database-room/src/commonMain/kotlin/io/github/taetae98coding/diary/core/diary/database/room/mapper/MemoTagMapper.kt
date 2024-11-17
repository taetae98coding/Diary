package io.github.taetae98coding.diary.core.diary.database.room.mapper

import io.github.taetae98coding.diary.core.diary.database.room.entity.MemoTagEntity
import io.github.taetae98coding.diary.core.model.memo.MemoTag

internal fun MemoTag.toEntity(): MemoTagEntity {
    return MemoTagEntity(
        memoId = memoId,
        tagId = tagId
    )
}
