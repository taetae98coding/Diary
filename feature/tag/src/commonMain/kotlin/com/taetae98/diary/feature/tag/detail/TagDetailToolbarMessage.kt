package com.taetae98.diary.feature.tag.detail

internal sealed class TagDetailToolbarMessage {
    abstract val messageShown: () -> Unit

    data class Delete(
        override val messageShown: () -> Unit,
    ) : TagDetailToolbarMessage()
}
