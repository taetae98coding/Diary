package io.github.taetae98coding.diary.domain.backup.repository

public interface MemoBackupRepository {
	public suspend fun backup(uid: String)

	public suspend fun upsertBackupQueue(uid: String, id: String)
}
