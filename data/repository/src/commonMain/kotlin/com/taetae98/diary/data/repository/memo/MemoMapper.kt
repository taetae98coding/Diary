package com.taetae98.diary.data.repository.memo

import com.taetae98.diary.data.dto.memo.MemoDto
import com.taetae98.diary.data.dto.memo.MemoStateDto
import com.taetae98.diary.domain.entity.memo.Memo
import com.taetae98.diary.domain.entity.memo.MemoState
import com.taetae98.diary.library.firestore.api.FireStoreData
import com.taetae98.diary.library.firestore.api.ext.toFireStoreTimestamp
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal fun MemoState.toDto(): MemoStateDto {
    return when (this) {
        MemoState.INCOMPLETE -> MemoStateDto.INCOMPLETE
        MemoState.COMPLETE -> MemoStateDto.COMPLETE
        MemoState.DELETE -> MemoStateDto.DELETE
    }
}

internal fun Memo.toDto(): MemoDto {
    return MemoDto(
        id = id,
        title = title,
        description = description,
        dateRangeColor = dateRangeColor,
        dateRange = dateRange,
        ownerId = ownerId,
        state = state.toDto(),
        updateAt = Clock.System.now()
    )
}

internal fun MemoStateDto.toDomain(): MemoState {
    return when (this) {
        MemoStateDto.INCOMPLETE -> MemoState.INCOMPLETE
        MemoStateDto.COMPLETE -> MemoState.COMPLETE
        MemoStateDto.DELETE -> MemoState.DELETE
    }
}

internal fun MemoDto.toDomain(): Memo {
    return Memo(
        id = id,
        title = title,
        description = description,
        dateRangeColor = dateRangeColor,
        dateRange = dateRange,
        ownerId = ownerId,
        state = state.toDomain(),
    )
}

internal fun MemoStateDto.toFireStore(): MemoFireStoreStateEntity {
    return when (this) {
        MemoStateDto.INCOMPLETE -> MemoFireStoreStateEntity.INCOMPLETE
        MemoStateDto.COMPLETE -> MemoFireStoreStateEntity.COMPLETE
        MemoStateDto.DELETE -> MemoFireStoreStateEntity.DELETE
    }
}

internal fun MemoFireStoreStateEntity.toDto(): MemoStateDto {
    return when (this) {
        MemoFireStoreStateEntity.INCOMPLETE -> MemoStateDto.INCOMPLETE
        MemoFireStoreStateEntity.COMPLETE -> MemoStateDto.COMPLETE
        MemoFireStoreStateEntity.DELETE -> MemoStateDto.DELETE
    }
}

internal fun MemoDto.toFireStore(): Map<String, Any?> {
    return mapOf(
        MemoFireStore.ID to id,
        MemoFireStore.TITLE to title,
        MemoFireStore.DESCRIPTION to description,
        MemoFireStore.DATE_RANGE_COLOR to dateRangeColor,
        MemoFireStore.DATE_RANGE_START to dateRange?.start?.toFireStoreTimestamp(),
        MemoFireStore.DATE_RANGE_END to dateRange?.endInclusive?.toFireStoreTimestamp(),
        MemoFireStore.STATE to state.toFireStore().value,
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
        title = getString(MemoFireStore.TITLE).orEmpty(),
        description = getString(MemoFireStore.DESCRIPTION).orEmpty(),
        dateRangeColor = getLong(MemoFireStore.DATE_RANGE_COLOR),
        dateRange = dateRange,
        state = MemoFireStoreStateEntity.valueOf(requireNotNull(getLong(MemoFireStore.STATE))).toDto(),
        ownerId = getString(MemoFireStore.OWNER_ID),
        updateAt = requireNotNull(getInstant(MemoFireStore.UPDATE_AT)),
    )
}
