package io.github.taetae98coding.diary.core.diary.database.room.dao

import io.github.taetae98coding.diary.core.diary.database.MemoBackupDao
import io.github.taetae98coding.diary.core.diary.database.room.DiaryDatabase
import io.github.taetae98coding.diary.core.diary.database.room.entity.MemoBackupEntity
import io.github.taetae98coding.diary.core.diary.database.room.entity.MemoEntity
import io.github.taetae98coding.diary.core.diary.database.room.mapper.toDto
import io.github.taetae98coding.diary.core.model.memo.MemoDto
import io.github.taetae98coding.diary.library.coroutines.mapCollectionLatest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Factory

@Factory
internal class MemoBackupRoomDao(
    private val database: DiaryDatabase,
) : MemoBackupDao {
    override suspend fun upsert(uid: String, memoId: String) {
        database.memoBackup().upsert(MemoBackupEntity(uid, memoId))
        getInternalUpdateFlow(uid).update { it + 1 }
    }

    override suspend fun delete(uid: String, memoId: String) {
        database.memoBackup().delete(MemoBackupEntity(uid, memoId))
    }

    override suspend fun deleteByMemoIds(memoIds: List<String>) {
        database.memoBackup().deleteByMemoIds(memoIds)
    }

    override fun getUpdateFlow(uid: String): Flow<Int> {
        return getInternalUpdateFlow(uid).asStateFlow()
    }

    override fun countByUid(uid: String): Flow<Int> {
        return database.memoBackup().countByUid(uid)
    }

    override fun findByUid(uid: String): Flow<List<MemoDto>> {
        return database.memoBackup().findByUid(uid)
            .mapCollectionLatest(MemoEntity::toDto)
    }

    companion object {
        private val updateFlow = mutableMapOf<String, MutableStateFlow<Int>>()

        private fun getInternalUpdateFlow(uid: String): MutableStateFlow<Int> {
            return updateFlow.getOrPut(uid) { MutableStateFlow(0) }
        }
    }
}
