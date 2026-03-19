package io.github.taetae98coding.diary.data.memo.repository

import io.github.taetae98coding.diary.core.database.api.DatabaseTransactor
import io.github.taetae98coding.diary.core.database.api.datasource.AccountMemoLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.MemoLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.SyncMemoLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.AccountMemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.MemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncMemoLocalEntity
import io.github.taetae98coding.diary.core.mapper.toLocal
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.domain.memo.repository.AccountMemoRepository
import kotlin.time.Clock
import kotlin.uuid.Uuid
import org.koin.core.annotation.Factory

@Factory
public class AccountMemoRepositoryImpl(
    private val databaseTransactor: DatabaseTransactor,
    private val memoLocalDataSource: MemoLocalDataSource,
    private val accountMemoLocalDataSource: AccountMemoLocalDataSource,
    private val syncMemoLocalDataSource: SyncMemoLocalDataSource,
) : AccountMemoRepository {
    override suspend fun add(
        accountId: Uuid,
        detail: MemoDetail,
    ) {
        val memoId = Uuid.random()
        val now = Clock.System.now().toEpochMilliseconds()

        databaseTransactor.writeTransaction {
            memoLocalDataSource.upsert(
                MemoLocalEntity(
                    id = memoId,
                    detail = detail.toLocal(),
                    createdAt = now,
                    updatedAt = now,
                ),
            )
            accountMemoLocalDataSource.upsert(
                AccountMemoLocalEntity(
                    accountId = accountId,
                    memoId = memoId,
                ),
            )
            syncMemoLocalDataSource.upsert(
                SyncMemoLocalEntity(memoId = memoId),
            )
        }
    }

    override suspend fun updateDetail(
        memoId: Uuid,
        detail: MemoDetail,
    ) {
        databaseTransactor.writeTransaction {
            memoLocalDataSource.updateDetail(
                memoId = memoId,
                detail = detail.toLocal(),
                updatedAt = Clock.System.now().toEpochMilliseconds(),
            )
            syncMemoLocalDataSource.upsert(
                SyncMemoLocalEntity(memoId = memoId),
            )
        }
    }

    override suspend fun updateFinish(
        memoId: Uuid,
        isFinished: Boolean,
    ) {
        databaseTransactor.writeTransaction {
            memoLocalDataSource.updateFinish(
                memoId = memoId,
                isFinished = isFinished,
                updatedAt = Clock.System.now().toEpochMilliseconds(),
            )
            syncMemoLocalDataSource.upsert(
                SyncMemoLocalEntity(memoId = memoId),
            )
        }
    }

    override suspend fun updateDelete(
        memoId: Uuid,
        isDeleted: Boolean,
    ) {
        databaseTransactor.writeTransaction {
            memoLocalDataSource.updateDelete(
                memoId = memoId,
                isDeleted = isDeleted,
                updatedAt = Clock.System.now().toEpochMilliseconds(),
            )
            syncMemoLocalDataSource.upsert(
                SyncMemoLocalEntity(memoId = memoId),
            )
        }
    }
}
