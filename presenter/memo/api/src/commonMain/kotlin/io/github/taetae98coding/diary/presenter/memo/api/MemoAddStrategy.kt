package io.github.taetae98coding.diary.presenter.memo.api

import io.github.taetae98coding.diary.core.model.memo.MemoDetail

public interface MemoAddStrategy {
    public suspend fun add(detail: MemoDetail): Result<Unit>
}
