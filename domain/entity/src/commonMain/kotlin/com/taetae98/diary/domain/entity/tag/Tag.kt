package com.taetae98.diary.domain.entity.tag

public data class Tag(
    val id: String,
    val title: String,
    val description: String,
    val state: TagState,
    val ownerId: String?,
)
