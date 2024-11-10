package io.github.taetae98coding.diary.data.memo.repository

import io.github.taetae98coding.diary.core.diary.database.MemoBackupDao
import io.github.taetae98coding.diary.core.diary.database.MemoDao
import io.github.taetae98coding.diary.core.model.mapper.toMemo
import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.core.model.memo.MemoDto
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import io.github.taetae98coding.diary.library.coroutines.mapCollectionLatest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.datetime.LocalDate
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
internal class MemoRepositoryImpl(
    private val localDataSource: MemoDao,
    private val backupDataSource: MemoBackupDao,
) : MemoRepository {
    override suspend fun upsert(memo: Memo) {
        val dto = MemoDto(
            id = memo.id,
            detail = memo.detail,
            owner = memo.owner,
            isFinish = memo.isFinish,
            isDelete = memo.isDelete,
            updateAt = memo.updateAt,
            serverUpdateAt = null,
        )

        localDataSource.upsert(dto)
    }

    override suspend fun update(memoId: String, detail: MemoDetail) {
        localDataSource.update(memoId, detail)
    }

    override suspend fun updateFinish(memoId: String, isFinish: Boolean) {
        localDataSource.updateFinish(memoId, isFinish)
    }

    override suspend fun updateDelete(memoId: String, isDelete: Boolean) {
        localDataSource.updateDelete(memoId, isDelete)
    }

    override fun find(memoId: String): Flow<Memo?> {
        return localDataSource.find(memoId)
            .mapLatest { it?.toMemo() }
    }

    override fun findByDateRange(owner: String?, dateRange: ClosedRange<LocalDate>): Flow<List<Memo>> {
        return localDataSource.findByDateRange(owner, dateRange)
            .mapCollectionLatest(MemoDto::toMemo)
    }
}
