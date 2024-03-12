package com.taetae98.diary.data.repository.memo

import com.taetae98.diary.data.dto.memo.MemoDto
import com.taetae98.diary.domain.entity.memo.Memo
import com.taetae98.diary.library.firestore.api.FireStoreData
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal fun Memo.toDto(): MemoDto {
    return MemoDto(
        id = id,
        title = title,
        description = description,
        dateRangeColor = dateRangeColor,
        dateRange = dateRange,
        ownerId = ownerId,
        isFinished = isFinished,
        isDeleted = false,
        updateAt = Clock.System.now(),
    )
}

internal fun MemoDto.toDomain(): Memo {
    return Memo(
        id = id,
        title = title,
        description = description,
        dateRangeColor = dateRangeColor,
        dateRange = dateRange,
        isFinished = isFinished,
        ownerId = ownerId,
    )
}

