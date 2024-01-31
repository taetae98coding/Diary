package com.taetae98.diary.feature.tag.detail

internal sealed class TagDetailUiState {
    abstract val message: TagDetailMessage?
    abstract val onMessageShown: () -> Unit

    data class Add(
        val onAdd: () -> Unit,
        override val message: TagDetailMessage?,
        override val onMessageShown: () -> Unit,
    ) : TagDetailUiState()
}