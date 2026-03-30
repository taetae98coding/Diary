package io.github.taetae98coding.diary.domain.sync

import io.github.taetae98coding.diary.core.model.sync.SyncStatus
import io.github.taetae98coding.diary.core.model.sync.SyncType
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

public interface SyncManager {
    public val syncStatus: Flow<SyncStatus>

    public fun requestSync(
        accountId: Uuid,
        type: SyncType,
    )
}
