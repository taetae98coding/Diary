package io.github.taetae98coding.diary.data.backup.repository

import io.github.taetae98coding.diary.core.backup.database.MemoBackupDao
import io.github.taetae98coding.diary.core.diary.database.MemoDao
import io.github.taetae98coding.diary.core.diary.service.memo.MemoService
import io.github.taetae98coding.diary.domain.backup.repository.MemoBackupRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.core.annotation.Factory

@Factory
internal class MemoBackupRepositoryImpl(
    private val memoDao: MemoDao,
    private val memoBackupDao: MemoBackupDao,
    private val memoService: MemoService,
) : MemoBackupRepository {
    override suspend fun backup(uid: String) {
        mutex.withLock {
            while (true) {
                val ids = memoBackupDao.findMemoIdByUid(uid).first()
                if (ids.isEmpty()) {
                    break
                }

                memoService.upsert(memoDao.findMemoAndTagIdsByIds(ids.toSet()).first())
                memoBackupDao.delete(ids.toSet())
            }
        }
    }

    override suspend fun upsertBackupQueue(uid: String, id: String) {
        memoBackupDao.upsert(uid, id)
    }

    companion object {
        private val mutex = Mutex()
    }
}
