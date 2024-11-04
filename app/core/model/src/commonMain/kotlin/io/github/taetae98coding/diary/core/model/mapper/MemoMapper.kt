package io.github.taetae98coding.diary.core.model.mapper

import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.model.memo.MemoDto

public fun MemoDto.toMemo(): Memo {
    return Memo(
        id = id,
        detail = detail,
        owner = owner,
        isFinish = isFinish,
        isDelete = isDelete,
        updateAt = updateAt,
    )
}
