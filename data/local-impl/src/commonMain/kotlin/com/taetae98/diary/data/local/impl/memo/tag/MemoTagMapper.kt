package com.taetae98.diary.data.local.impl.memo.tag

import com.taetae98.diary.data.dto.memo.MemoTagDto
import com.taetae98.diary.data.local.impl.MemoTagEntity

internal fun MemoTagEntity.toDto(): MemoTagDto {
    return MemoTagDto(
        memoId = memoId,
        tagId = tagId,
    )
}
