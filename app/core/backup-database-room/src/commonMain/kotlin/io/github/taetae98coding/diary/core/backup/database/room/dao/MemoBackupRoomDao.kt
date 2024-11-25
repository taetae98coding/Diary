package io.github.taetae98coding.diary.core.backup.database.room.dao

import io.github.taetae98coding.diary.core.backup.database.MemoBackupDao
import io.github.taetae98coding.diary.core.backup.database.room.BackupDatabase
import io.github.taetae98coding.diary.core.backup.database.room.entity.MemoBackupEntity
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class MemoBackupRoomDao(
	private val database: BackupDatabase,
) : MemoBackupDao {
	override suspend fun upsert(uid: String, id: String) {
		database.memo().upsert(MemoBackupEntity(memoId = id, uid = uid))
	}

	override suspend fun delete(ids: Set<String>) {
		database.memo().delete(ids)
	}

	override fun findMemoIdByUid(uid: String): Flow<List<String>> = database.memo().findMemoIdByUid(uid)
}
