package io.github.taetae98coding.diary.core.model.memo

import kotlin.uuid.Uuid

public data class Memo(
    val id: Uuid,
    val detail: MemoDetail,
    val isFinished: Boolean = false,
    val isDeleted: Boolean = false,
    val updatedAt: Long,
    val createdAt: Long,
)
