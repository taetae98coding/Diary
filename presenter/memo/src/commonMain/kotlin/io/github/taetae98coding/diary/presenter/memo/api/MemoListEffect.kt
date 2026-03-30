package io.github.taetae98coding.diary.presenter.memo.api

import kotlin.uuid.Uuid

public sealed class MemoListEffect {
    public data object None : MemoListEffect()
    public data class FinishComplete(val memoId: Uuid) : MemoListEffect()
    public data class DeleteComplete(val memoId: Uuid) : MemoListEffect()
}
