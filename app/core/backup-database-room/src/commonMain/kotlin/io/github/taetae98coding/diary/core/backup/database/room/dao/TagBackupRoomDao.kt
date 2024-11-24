package io.github.taetae98coding.diary.core.backup.database.room.dao

import io.github.taetae98coding.diary.core.backup.database.TagBackupDao
import io.github.taetae98coding.diary.core.backup.database.room.BackupDatabase
import io.github.taetae98coding.diary.core.backup.database.room.entity.TagBackupEntity
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class TagBackupRoomDao(private val database: BackupDatabase) : TagBackupDao {
	override suspend fun upsert(uid: String, id: String) {
		database.tag().upsert(TagBackupEntity(tagId = id, uid = uid))
	}

	override suspend fun delete(ids: Set<String>) {
		database.tag().delete(ids)
	}

	override fun findByUid(uid: String): Flow<List<String>> = database.tag().findByUid(uid)
}
