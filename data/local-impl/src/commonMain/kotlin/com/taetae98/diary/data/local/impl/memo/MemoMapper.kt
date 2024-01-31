package com.taetae98.diary.data.local.impl.memo

import com.taetae98.diary.data.dto.memo.MemoDto
import com.taetae98.diary.data.dto.memo.MemoStateDto
import com.taetae98.diary.data.local.impl.MemoEntity
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

internal fun MemoStateDto.toEntity(): MemoStateEntity {
    return when (this) {
        MemoStateDto.NONE -> MemoStateEntity.NONE
        MemoStateDto.COMPLETE -> MemoStateEntity.COMPLETE
        MemoStateDto.DELETE -> MemoStateEntity.DELETE
    }
}

internal fun MemoDto.toEntity(): MemoEntity {
    return MemoEntity(
        id = id,
        title = title,
        description = description,
        dateRangeColor = dateRangeColor,
        dateRangeStart = dateRange?.start,
        dateRangeEnd = dateRange?.endInclusive,
        state = state.toEntity(),
        ownerId = ownerId,
        updateAt = updateAt,
    )
}

internal fun MemoStateEntity.toDto(): MemoStateDto {
    return when (this) {
        MemoStateEntity.NONE -> MemoStateDto.NONE
        MemoStateEntity.COMPLETE -> MemoStateDto.COMPLETE
        MemoStateEntity.DELETE -> MemoStateDto.DELETE
    }
}

internal fun mapToMemoDto(
    id: String,
    title: String,
    description: String,
    dateRangeColor: Long?,
    dateRangeStart: LocalDate?,
    dateRangeEnd: LocalDate?,
    state: MemoStateEntity,
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
        state = state.toDto(),
        ownerId = owner,
        updateAt = updateAt,
    )
}
