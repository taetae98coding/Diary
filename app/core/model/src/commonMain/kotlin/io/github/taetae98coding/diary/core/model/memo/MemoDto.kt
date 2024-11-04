package io.github.taetae98coding.diary.core.model.memo

import kotlinx.datetime.Instant

public data class MemoDto(
    val id: String,
    val detail: MemoDetail,
    val owner: String?,
    val isFinish: Boolean,
    val isDelete: Boolean,
    val updateAt: Instant,
    val serverUpdateAt: Instant?,
)
