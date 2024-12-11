package io.github.taetae98coding.diary.core.model.mapper

import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.model.memo.MemoDto

public fun Memo.toDto(): MemoDto =
	MemoDto(
		id = id,
		detail = detail,
		primaryTag = primaryTag,
		isFinish = isFinish,
		isDelete = isDelete,
		updateAt = updateAt,
		serverUpdateAt = null,
	)

public fun MemoDto.toMemo(): Memo =
	Memo(
		id = id,
		detail = detail,
		primaryTag = primaryTag,
		isFinish = isFinish,
		isDelete = isDelete,
		updateAt = updateAt,
	)
