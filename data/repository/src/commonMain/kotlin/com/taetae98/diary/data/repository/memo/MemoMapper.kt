package com.taetae98.diary.data.repository.memo

import com.taetae98.diary.data.dto.memo.MemoDto
import com.taetae98.diary.domain.entity.account.Memo
import kotlinx.datetime.Clock

internal fun Memo.toDto(): MemoDto {
    return MemoDto(
        id = id,
        title = title,
        updateAt = Clock.System.now()
    )
}