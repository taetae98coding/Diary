package io.github.taetae98coding.diary.domain.backup.repository

public interface TagBackupRepository {
    public suspend fun backup(uid: String)
    public suspend fun upsertBackupQueue(uid: String, id: String)
}
