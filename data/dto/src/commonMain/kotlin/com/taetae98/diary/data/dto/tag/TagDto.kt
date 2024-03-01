package com.taetae98.diary.data.dto.tag

import kotlinx.datetime.Instant

public data class TagDto(
    val id: String,
    val title: String,
    val description: String,
    val isDeleted: Boolean,
    val ownerId: String?,
    val updateAt: Instant,
)
