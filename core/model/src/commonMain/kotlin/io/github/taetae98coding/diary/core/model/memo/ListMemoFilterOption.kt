package io.github.taetae98coding.diary.core.model.memo

import io.github.taetae98coding.diary.core.model.FilterPresence

public data class ListMemoFilterOption(
    val tagPresence: FilterPresence = FilterPresence.NONE,
    val datePresence: FilterPresence = FilterPresence.NONE,
)
