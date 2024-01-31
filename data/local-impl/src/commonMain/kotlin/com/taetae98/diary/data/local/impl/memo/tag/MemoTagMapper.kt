package com.taetae98.diary.data.local.impl.memo.tag

import com.taetae98.diary.data.dto.memo.MemoTagDto
import com.taetae98.diary.data.local.impl.MemoTagEntity

internal fun MemoTagDto.toEntity(): MemoTagEntity {
    return MemoTagEntity(
        memoId = memoId,
        tagId = tagId,
    )
}
