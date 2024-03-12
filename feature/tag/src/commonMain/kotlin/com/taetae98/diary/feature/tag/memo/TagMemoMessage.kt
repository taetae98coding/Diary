package com.taetae98.diary.feature.tag.memo

internal sealed class TagMemoMessage {
    abstract val onMessageShown: () -> Unit

    data class NotFound(
        override val onMessageShown: () -> Unit,
    ) : TagMemoMessage()
}