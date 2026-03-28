package io.github.taetae98coding.diary.core.database.api.entity

import androidx.room3.ColumnInfo

public data class TagDetailLocalEntity(
    @ColumnInfo(name = "title", defaultValue = "")
    val title: String,
    @ColumnInfo(name = "description", defaultValue = "")
    val description: String,
    @ColumnInfo(name = "color", defaultValue = "0")
    val color: Int,
)
