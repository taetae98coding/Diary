package com.taetae98.diary.local.impl.memo

import com.taetae98.diary.data.dto.memo.MemoDto
import com.taetae98.diary.data.local.impl.MemoEntity

internal fun MemoDto.toEntity(): MemoEntity {
    return MemoEntity(
        id = id,
        title = title,
        updateAt = updateAt,
    )
}