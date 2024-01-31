package com.taetae98.diary.data.repository.memo.tag

import com.taetae98.diary.data.dto.memo.MemoTagDto
import com.taetae98.diary.domain.entity.memo.MemoTag

internal fun MemoTag.toDto(): MemoTagDto {
    return MemoTagDto(
        memoId = memoId,
        tagId = tagId,
    )
}