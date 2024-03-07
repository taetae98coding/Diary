package com.taetae98.diary.feature.memo.detail

internal sealed class MemoDetailToolbarMessage {
    abstract val messageShown: () -> Unit

    data class Delete(
        override val messageShown: () -> Unit,
    ) : MemoDetailToolbarMessage()
}