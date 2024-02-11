package com.taetae98.diary.data.repository.memo

import com.taetae98.diary.data.dto.memo.MemoDto
import com.taetae98.diary.domain.entity.memo.Memo
import com.taetae98.diary.library.firestore.api.FireStoreData
import com.taetae98.diary.library.firestore.api.ext.toFireStoreTimestamp
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
        updateAt = Clock.System.now()
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

internal fun MemoDto.toFireStore(): Map<String, Any?> {
    return mapOf(
        MemoFireStore.ID to id,
        MemoFireStore.TITLE to title,
        MemoFireStore.DESCRIPTION to description,
        MemoFireStore.DATE_RANGE_COLOR to dateRangeColor,
        MemoFireStore.DATE_RANGE_START to dateRange?.start?.toFireStoreTimestamp(),
        MemoFireStore.DATE_RANGE_END to dateRange?.endInclusive?.toFireStoreTimestamp(),
        MemoFireStore.OWNER_ID to ownerId,
        MemoFireStore.UPDATE_AT to updateAt.toFireStoreTimestamp(),
    )
}

internal fun FireStoreData.toMemoDto(): MemoDto {
    val dateRangeStart = getInstant(MemoFireStore.DATE_RANGE_START)?.toLocalDateTime(TimeZone.UTC)?.date
    val dateRangeEnd = getInstant(MemoFireStore.DATE_RANGE_END)?.toLocalDateTime(TimeZone.UTC)?.date
    val dateRange = if (dateRangeStart != null && dateRangeEnd != null) {
        dateRangeStart..dateRangeEnd
    } else {
        null
    }

    return MemoDto(
        id = requireNotNull(getString(MemoFireStore.ID)),
        title = requireNotNull(getString(MemoFireStore.TITLE)),
        description = requireNotNull(getString(MemoFireStore.DESCRIPTION)),
        dateRangeColor = getLong(MemoFireStore.DATE_RANGE_COLOR),
        dateRange = dateRange,
        isFinished = requireNotNull(getBoolean(MemoFireStore.IS_FINISHED)),
        isDeleted = getBoolean(MemoFireStore.IS_DELETED) ?: false,
        ownerId = getString(MemoFireStore.OWNER_ID),
        updateAt = requireNotNull(getInstant(MemoFireStore.UPDATE_AT)),
    )
}
