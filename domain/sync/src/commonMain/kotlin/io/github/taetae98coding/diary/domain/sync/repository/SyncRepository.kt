package io.github.taetae98coding.diary.domain.sync.repository

import kotlin.uuid.Uuid

public interface SyncRepository {
    public suspend fun sync(accountId: Uuid)
    public fun requestSync(accountId: Uuid)
}
