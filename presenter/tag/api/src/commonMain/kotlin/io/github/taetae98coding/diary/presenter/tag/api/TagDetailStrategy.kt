package io.github.taetae98coding.diary.presenter.tag.api

import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.core.model.tag.TagDetail
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

public interface TagDetailStrategy {
    public fun get(tagId: Uuid): Flow<Result<Tag?>>

    public suspend fun update(
        tagId: Uuid,
        detail: TagDetail,
    ): Result<Unit>

    public suspend fun finish(tagId: Uuid): Result<Unit>

    public suspend fun restart(tagId: Uuid): Result<Unit>

    public suspend fun delete(tagId: Uuid): Result<Unit>
}
