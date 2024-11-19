package io.github.taetae98coding.diary.data.backup.repository

import io.github.taetae98coding.diary.core.diary.database.MemoBackupDao
import io.github.taetae98coding.diary.core.diary.service.memo.MemoService
import io.github.taetae98coding.diary.core.model.mapper.toMemo
import io.github.taetae98coding.diary.core.model.memo.MemoDto
import io.github.taetae98coding.diary.domain.backup.repository.BackupRepository
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
internal class BackupRepositoryImpl(
    private val memoBackupDao: MemoBackupDao,
    private val memoService: MemoService,
) : BackupRepository {
    override suspend fun backup(uid: String) {
        while (memoBackupDao.countByUid(uid).first() > 0) {
            val memoList = memoBackupDao.findByUid(uid).first()
                .map(MemoDto::toMemo)

            memoService.upsert(memoList)
            memoBackupDao.deleteByMemoIds(memoList.map { it.id })
        }
    }

    override suspend fun upsertMemoBackupQueue(uid: String, memoId: String) {
        memoBackupDao.upsert(uid, memoId)
    }
}
