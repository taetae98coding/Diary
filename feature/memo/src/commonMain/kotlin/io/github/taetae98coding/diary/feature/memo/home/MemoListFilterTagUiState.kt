package io.github.taetae98coding.diary.feature.memo.home

import kotlin.uuid.Uuid

internal data class MemoListFilterTagUiState(
    val id: Uuid,
    val title: String,
    val color: Int,
    val isSelected: Boolean,
)
