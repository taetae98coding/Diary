package com.taetae98.diary.local.impl.memo

import com.taetae98.diary.data.dto.memo.MemoDto
import com.taetae98.diary.data.dto.memo.MemoStateDto
import com.taetae98.diary.data.local.impl.MemoEntity
import kotlinx.datetime.Instant

internal fun MemoStateDto.toEntity(): MemoStateEntity {
    return when (this) {
        MemoStateDto.INCOMPLETE -> MemoStateEntity.INCOMPLETE
        MemoStateDto.COMPLETE -> MemoStateEntity.COMPLETE
        MemoStateDto.DELETE -> MemoStateEntity.DELETE
    }
}

internal fun MemoDto.toEntity(): MemoEntity {
    return MemoEntity(
        id = id,
        title = title,
        state = state.toEntity(),
        ownerId = ownerId,
        updateAt = updateAt,
    )
}

internal fun MemoStateEntity.toDto(): MemoStateDto {
    return when (this) {
        MemoStateEntity.INCOMPLETE -> MemoStateDto.INCOMPLETE
        MemoStateEntity.COMPLETE -> MemoStateDto.COMPLETE
        MemoStateEntity.DELETE -> MemoStateDto.DELETE
    }
}

internal fun mapToMemoDto(
    id: String,
    title: String,
    state: MemoStateEntity,
    owner: String?,
    updateAt: Instant
): MemoDto {
    return MemoDto(
        id = id,
        title = title,
        state = state.toDto(),
        ownerId = owner,
        updateAt = updateAt,
    )
}
