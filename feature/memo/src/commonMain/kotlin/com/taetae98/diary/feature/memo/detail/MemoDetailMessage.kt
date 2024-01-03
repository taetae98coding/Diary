package com.taetae98.diary.feature.memo.detail

internal sealed class MemoDetailMessage {
    data object Add : MemoDetailMessage()
    data object Delete : MemoDetailMessage()
    data object TitleEmpty : MemoDetailMessage()
    data object Update : MemoDetailMessage()
}