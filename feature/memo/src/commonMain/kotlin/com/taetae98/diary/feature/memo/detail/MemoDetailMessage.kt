package com.taetae98.diary.feature.memo.detail

internal sealed class MemoDetailMessage {
    abstract val messageShown: () -> Unit

    data class Add(
        override val messageShown: () -> Unit,
    ) : MemoDetailMessage()

    data class TitleEmpty(
        override val messageShown: () -> Unit,
    ) : MemoDetailMessage()

    data class Update(
        override val messageShown: () -> Unit,
    ) : MemoDetailMessage()

    data class UpdateFail(
        override val messageShown: () -> Unit,
    ) : MemoDetailMessage()
}