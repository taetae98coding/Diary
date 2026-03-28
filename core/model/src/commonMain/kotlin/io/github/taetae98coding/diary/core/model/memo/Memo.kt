package io.github.taetae98coding.diary.core.model.memo

import kotlin.uuid.Uuid

public data class Memo(
    val id: Uuid,
    val detail: MemoDetail,
    val primaryTag: Uuid?,
    val isFinished: Boolean,
    val isDeleted: Boolean,
    val updatedAt: Long,
    val createdAt: Long,
)
