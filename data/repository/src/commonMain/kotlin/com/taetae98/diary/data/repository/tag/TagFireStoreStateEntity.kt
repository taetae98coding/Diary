package com.taetae98.diary.data.repository.tag

internal enum class TagFireStoreStateEntity(
    val value: Long
) {
    NONE(0L),
    DELETE(1L),
}