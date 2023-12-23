package com.taetae98.diary.data.repository.memo

import com.taetae98.diary.data.dto.memo.MemoDto
import com.taetae98.diary.domain.entity.account.Memo
import com.taetae98.diary.library.firestore.api.ext.toFireStoreTimestamp
import kotlinx.datetime.Clock

internal fun Memo.toDto(): MemoDto {
    return MemoDto(
        id = id,
        title = title,
        updateAt = Clock.System.now()
    )
}

internal fun MemoDto.toDomain(): Memo {
    return Memo(
        id = id,
        title = title,
    )
}

internal fun MemoDto.toFireStore(): Map<String, Any> {
    return mapOf(
        "id" to id,
        "title" to title,
        "updateAt" to updateAt.toFireStoreTimestamp(),
    )
}