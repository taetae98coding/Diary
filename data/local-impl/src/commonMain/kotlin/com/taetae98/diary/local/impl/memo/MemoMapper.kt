package com.taetae98.diary.local.impl.memo

import com.taetae98.diary.data.dto.memo.MemoDto
import com.taetae98.diary.data.dto.memo.MemoStateDto
import com.taetae98.diary.data.local.impl.MemoEntity
import kotlinx.datetime.Instant

internal fun MemoStateDto.toEntity(): MemoStateEntity {
    return when(this) {
        MemoStateDto.NONE -> MemoStateEntity.NONE
        MemoStateDto.FINISH -> MemoStateEntity.FINISH
        MemoStateDto.DELETE -> MemoStateEntity.DELETE
    }
}

internal fun MemoDto.toEntity(): MemoEntity {
    return MemoEntity(
        id = id,
        title = title,
        state = state.toEntity(),
        updateAt = updateAt,
    )
}

internal fun MemoStateEntity.toDto(): MemoStateDto {
    return when(this) {
        MemoStateEntity.NONE -> MemoStateDto.NONE
        MemoStateEntity.FINISH -> MemoStateDto.FINISH
        MemoStateEntity.DELETE -> MemoStateDto.DELETE
    }
}

internal fun mapToMemoDto(id: String, title: String, entity: MemoStateEntity, updateAt: Instant): MemoDto {
    return MemoDto(
        id,
        title,
        entity.toDto(),
        updateAt
    )
}
