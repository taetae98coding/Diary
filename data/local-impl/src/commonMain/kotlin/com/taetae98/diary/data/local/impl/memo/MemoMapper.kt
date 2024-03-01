package com.taetae98.diary.data.local.impl.memo

import com.taetae98.diary.data.dto.memo.MemoDto
import com.taetae98.diary.data.local.impl.MemoEntity
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

internal fun MemoDto.toEntity(): MemoEntity {
    return MemoEntity(
        id = id,
        title = title,
        description = description,
        dateRangeColor = dateRangeColor,
        dateRangeStart = dateRange?.start,
        dateRangeEnd = dateRange?.endInclusive,
        isFinished = isFinished,
        ownerId = ownerId,
        updateAt = updateAt,
    )
}

internal fun mapToMemoDto(
    id: String,
    title: String,
    description: String,
    dateRangeColor: Long?,
    dateRangeStart: LocalDate?,
    dateRangeEnd: LocalDate?,
    isFinished: Boolean,
    owner: String?,
    updateAt: Instant
): MemoDto {
    val dateRange = if (dateRangeStart != null && dateRangeEnd != null) {
        dateRangeStart..dateRangeEnd
    } else {
        null
    }

    return MemoDto(
        id = id,
        title = title,
        description = description,
        dateRangeColor = dateRangeColor,
        dateRange = dateRange,
        isFinished = isFinished,
        isDeleted = false,
        ownerId = owner,
        updateAt = updateAt,
    )
}
