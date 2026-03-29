package io.github.taetae98coding.diary.core.model.sync

public sealed class SyncStatus {
    public data object Idle : SyncStatus()
    public data object Syncing : SyncStatus()
}
