package io.github.taetae98coding.diary.core.model.memo

import kotlinx.datetime.Instant

public data class MemoTagDto(
    val memoId: String,
    val tagId: String,
    val isSelected: Boolean,
    val updateAt: Instant,
)
