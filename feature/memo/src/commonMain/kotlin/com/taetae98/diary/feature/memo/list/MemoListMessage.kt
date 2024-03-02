package com.taetae98.diary.feature.memo.list

internal sealed class MemoListMessage {
    data object Finish : MemoListMessage()
    data object Delete : MemoListMessage()
}