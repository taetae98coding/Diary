package com.taetae98.diary.data.repository.memo

import com.taetae98.diary.data.dto.memo.MemoDto
import com.taetae98.diary.data.dto.memo.MemoStateDto
import com.taetae98.diary.domain.entity.account.memo.Memo
import com.taetae98.diary.domain.entity.account.memo.MemoState
import com.taetae98.diary.library.firestore.api.ext.toFireStoreTimestamp
import kotlinx.datetime.Clock

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
        MemoFireStore.STATE to state.toFireStore().value,
        MemoFireStore.OWNER_ID to ownerId,
        MemoFireStore.UPDATE_AT to updateAt.toFireStoreTimestamp(),
    )
}
