package io.github.taetae98coding.diary.presenter.memo.api

import kotlin.uuid.Uuid

public data class MemoTagUiState(
    val tagId: Uuid,
    val title: String,
    val color: Int,
    val isPrimary: Boolean,
    val isSelected: Boolean,
)
