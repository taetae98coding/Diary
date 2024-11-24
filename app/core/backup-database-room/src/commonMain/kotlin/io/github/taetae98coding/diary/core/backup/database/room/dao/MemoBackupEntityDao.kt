package io.github.taetae98coding.diary.core.backup.database.room.dao

import androidx.room.Dao
import androidx.room.Query
import io.github.taetae98coding.diary.core.backup.database.room.entity.MemoBackupEntity
import io.github.taetae98coding.diary.library.room.dao.EntityDao
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class MemoBackupEntityDao : EntityDao<MemoBackupEntity>() {
	@Query("DELETE FROM MemoBackupEntity WHERE memoId IN (:memoIds)")
	abstract suspend fun delete(memoIds: Set<String>)

	@Query("SELECT memoId FROM MemoBackupEntity WHERE uid = :uid")
	abstract fun findMemoIdByUid(uid: String): Flow<List<String>>
}
