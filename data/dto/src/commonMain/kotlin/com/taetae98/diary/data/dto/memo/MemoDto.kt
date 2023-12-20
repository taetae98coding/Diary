package com.taetae98.diary.data.dto.memo

import kotlinx.datetime.Instant

public data class MemoDto(
    val id: String,
    val title: String,
    val updateAt: Instant,
)