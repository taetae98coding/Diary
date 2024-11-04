package io.github.taetae98coding.diary.data.backup.repository

import io.github.taetae98coding.diary.core.diary.database.MemoBackupDao
import io.github.taetae98coding.diary.core.diary.service.memo.MemoService
import io.github.taetae98coding.diary.core.model.mapper.toMemo
import io.github.taetae98coding.diary.core.model.memo.MemoDto
import io.github.taetae98coding.diary.domain.backup.repository.BackupRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
internal class BackupRepositoryImpl(
    private val memoBackupDao: MemoBackupDao,
    private val memoService: MemoService,
) : BackupRepository {
    override suspend fun backupMemo(uid: String) {
        while (memoBackupDao.countByUid(uid).first() > 0) {
            val memoList = memoBackupDao.findByUid(uid).first()
                .map(MemoDto::toMemo)

            memoService.upsert(memoList)
            memoBackupDao.deleteByMemoIds(memoList.map { it.id })
        }
    }

    override fun getUpdateFlow(uid: String): Flow<Unit> {
        return memoBackupDao.getUpdateFlow(uid)
            .mapLatest { }
    }

    override fun countBackupMemo(uid: String): Flow<Int> {
        return memoBackupDao.countByUid(uid)
    }
}
