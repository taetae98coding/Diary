package io.github.taetae98coding.diary.core.diary.database.room.dao

import io.github.taetae98coding.diary.core.diary.database.MemoBackupDao
import io.github.taetae98coding.diary.core.diary.database.room.DiaryDatabase
import io.github.taetae98coding.diary.core.diary.database.room.entity.MemoBackupEntity
import io.github.taetae98coding.diary.core.diary.database.room.entity.MemoEntity
import io.github.taetae98coding.diary.core.diary.database.room.mapper.toDto
import io.github.taetae98coding.diary.core.model.memo.MemoDto
import io.github.taetae98coding.diary.library.coroutines.mapCollectionLatest
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class MemoBackupRoomDao(
    private val database: DiaryDatabase,
) : MemoBackupDao {
    override suspend fun upsert(uid: String, memoId: String) {
        database.memoBackup().upsert(MemoBackupEntity(uid, memoId))
    }

    override suspend fun deleteByMemoIds(memoIds: List<String>) {
        database.memoBackup().deleteByMemoIds(memoIds)
    }

    override fun countByUid(uid: String): Flow<Int> {
        return database.memoBackup().countByUid(uid)
    }

    override fun findByUid(uid: String): Flow<List<MemoDto>> {
        return database.memoBackup().findByUid(uid)
            .mapCollectionLatest(MemoEntity::toDto)
    }
}
