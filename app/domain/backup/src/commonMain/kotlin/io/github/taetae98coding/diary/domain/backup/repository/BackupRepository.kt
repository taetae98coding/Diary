package io.github.taetae98coding.diary.domain.backup.repository

import kotlinx.coroutines.flow.Flow

public interface BackupRepository {
    public suspend fun backupMemo(uid: String)

    public fun getUpdateFlow(uid: String): Flow<Unit>
    public fun countBackupMemo(uid: String): Flow<Int>
}
