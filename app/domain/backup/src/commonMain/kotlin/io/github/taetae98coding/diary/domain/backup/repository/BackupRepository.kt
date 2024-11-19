package io.github.taetae98coding.diary.domain.backup.repository

public interface BackupRepository {
    public suspend fun backup(uid: String)
    public suspend fun upsertMemoBackupQueue(uid: String, memoId: String)
}
