package io.github.taetae98coding.diary.core.model.tag

import kotlin.uuid.Uuid

public data class Tag(
    val id: Uuid,
    val detail: TagDetail,
    val isFinished: Boolean = false,
    val isDeleted: Boolean = false,
    val updatedAt: Long,
    val createdAt: Long,
)
