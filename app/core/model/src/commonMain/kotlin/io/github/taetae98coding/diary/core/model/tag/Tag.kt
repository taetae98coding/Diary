package io.github.taetae98coding.diary.core.model.tag

import kotlinx.datetime.Instant

public data class Tag(
    val id: String,
    val detail: TagDetail,
    val owner: String?,
    val isFinish: Boolean,
    val isDelete: Boolean,
    val updateAt: Instant,
)
