package com.taetae98.diary.feature.tag.detail

internal sealed class TagDetailMessage {
    abstract val messageShown: () -> Unit

    data class Upsert(
        override val messageShown: () -> Unit,
    ) : TagDetailMessage()

    data class UpsertFail(
        override val messageShown: () -> Unit,
    ) : TagDetailMessage()
}
