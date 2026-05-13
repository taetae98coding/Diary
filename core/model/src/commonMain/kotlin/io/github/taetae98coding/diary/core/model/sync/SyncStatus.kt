package io.github.taetae98coding.diary.core.model.sync

public sealed interface SyncStatus {
    public data object Idle : SyncStatus
    public data class Syncing(val type: SyncType) : SyncStatus
    public data class Failed(val type: SyncType) : SyncStatus
}
