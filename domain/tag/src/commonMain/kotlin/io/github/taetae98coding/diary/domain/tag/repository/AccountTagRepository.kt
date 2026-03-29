package io.github.taetae98coding.diary.domain.tag.repository

import androidx.paging.PagingData
import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.core.model.tag.TagDetail
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

public interface AccountTagRepository {
    public suspend fun add(
        accountId: Uuid,
        detail: TagDetail,
    )

    public suspend fun updateDetail(
        tagId: Uuid,
        detail: TagDetail,
    )

    public suspend fun updateFinish(
        tagId: Uuid,
        isFinished: Boolean,
    )

    public suspend fun updateDelete(
        tagId: Uuid,
        isDeleted: Boolean,
    )

    public fun page(accountId: Uuid): Flow<PagingData<Tag>>
}
