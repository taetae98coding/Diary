package com.taetae98.diary.feature.tag.add

internal sealed class TagAddMessage {
    abstract val onMessageShown: () -> Unit

    data class Add(
        override val onMessageShown: () -> Unit,
    ) : TagAddMessage()

    data class TitleEmpty(
        override val onMessageShown: () -> Unit,
    ) : TagAddMessage()
}
