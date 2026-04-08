package io.github.taetae98coding.diary.feature.memo.common

import kotlin.uuid.Uuid

internal data class MemoTagUiState(
    val tagId: Uuid,
    val title: String,
    val color: Int,
    val isPrimary: Boolean,
    val isSelected: Boolean,
)
