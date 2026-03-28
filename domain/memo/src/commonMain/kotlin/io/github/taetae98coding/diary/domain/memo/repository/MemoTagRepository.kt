package io.github.taetae98coding.diary.domain.memo.repository

import io.github.taetae98coding.diary.core.model.tag.Tag
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

public interface MemoTagRepository {
    public fun getMemoTag(memoId: Uuid): Flow<List<Tag>>
}
