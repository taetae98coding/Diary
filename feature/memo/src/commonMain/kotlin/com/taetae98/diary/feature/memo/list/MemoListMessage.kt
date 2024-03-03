package com.taetae98.diary.feature.memo.list

internal sealed class MemoListMessage {
    data class Finish(
        val cancel: () -> Unit,
    ) : MemoListMessage()

    data class Delete(
        val cancel: () -> Unit,
    ) : MemoListMessage()
}