package io.github.taetae98coding.diary.presenter.memo.api

import androidx.paging.PagingData
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.core.model.tag.Tag
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

public interface MemoAddStrategy {
    public suspend fun add(
        detail: MemoDetail,
        primaryTag: Uuid?,
        memoTagIds: Set<Uuid>,
    ): Result<Unit>
    public fun pageTag(): Flow<Result<PagingData<Tag>>>
}
