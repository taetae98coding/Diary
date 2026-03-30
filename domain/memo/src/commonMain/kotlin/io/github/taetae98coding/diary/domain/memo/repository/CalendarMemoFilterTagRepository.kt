package io.github.taetae98coding.diary.domain.memo.repository

import kotlin.uuid.Uuid

public interface CalendarMemoFilterTagRepository {
    public suspend fun upsert(tagId: Uuid)
    public suspend fun delete(tagId: Uuid)
}
