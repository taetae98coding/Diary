package io.github.taetae98coding.diary.core.model.mapper

import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.model.memo.MemoDto

public fun Memo.toDto(): MemoDto {
    return MemoDto(
        id = id,
        detail = detail,
        owner = owner,
        primaryTag = primaryTag,
        isFinish = isFinish,
        isDelete = isDelete,
        updateAt = updateAt,
        serverUpdateAt = null,
    )
}

public fun MemoDto.toMemo(): Memo {
    return Memo(
        id = id,
        detail = detail,
        owner = owner,
        primaryTag = primaryTag,
        isFinish = isFinish,
        isDelete = isDelete,
        updateAt = updateAt,
    )
}
