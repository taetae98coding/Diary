package io.github.taetae98coding.diary.presenter.memo.api

import kotlinx.coroutines.flow.Flow

public interface ListMemoFilterStrategy {

    public fun hasFilter(): Flow<Result<Boolean>>
}
