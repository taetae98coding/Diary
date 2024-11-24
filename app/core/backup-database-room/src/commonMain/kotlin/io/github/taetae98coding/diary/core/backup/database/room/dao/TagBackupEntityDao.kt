package io.github.taetae98coding.diary.core.backup.database.room.dao

import androidx.room.Dao
import androidx.room.Query
import io.github.taetae98coding.diary.core.backup.database.room.entity.TagBackupEntity
import io.github.taetae98coding.diary.library.room.dao.EntityDao
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class TagBackupEntityDao : EntityDao<TagBackupEntity>() {
	@Query("DELETE FROM TagBackupEntity WHERE tagId IN (:tagIds)")
	abstract suspend fun delete(tagIds: Set<String>)

	@Query("SELECT COUNT(DISTINCT uid) FROM TagBackupEntity WHERE uid = :uid")
	abstract fun countByUid(uid: String): Flow<Int>

	@Query("SELECT DISTINCT tagId FROM TagBackupEntity WHERE uid = :uid LIMIT 50")
	abstract fun findByUid(uid: String): Flow<List<String>>
}
