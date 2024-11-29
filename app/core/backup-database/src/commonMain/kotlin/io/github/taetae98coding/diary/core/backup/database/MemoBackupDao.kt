package io.github.taetae98coding.diary.core.backup.database

import kotlinx.coroutines.flow.Flow

public interface MemoBackupDao {
	public suspend fun upsert(uid: String, id: String)

	public suspend fun delete(ids: Set<String>)

	public fun getMemoIdByUid(uid: String): Flow<List<String>>
}
