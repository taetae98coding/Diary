package io.github.taetae98coding.diary.core.model.sync

public sealed class SyncStatus {
    public data object Idle : SyncStatus()
    public data class Syncing(val type: SyncType) : SyncStatus()
    public data object Failed : SyncStatus()
}
