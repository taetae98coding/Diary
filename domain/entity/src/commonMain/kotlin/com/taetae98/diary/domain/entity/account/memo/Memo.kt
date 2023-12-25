package com.taetae98.diary.domain.entity.account.memo

public data class Memo(
    val id: String,
    val title: String,
    val ownerId: String?,
    val state: MemoState,
)
