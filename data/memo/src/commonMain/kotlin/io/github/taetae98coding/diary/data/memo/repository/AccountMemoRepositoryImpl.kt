package io.github.taetae98coding.diary.data.memo.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import io.github.taetae98coding.diary.core.database.api.DatabaseTransactor
import io.github.taetae98coding.diary.core.database.api.datasource.AccountMemoLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.MemoLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.MemoTagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.SyncMemoLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.SyncMemoTagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.AccountMemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.MemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.MemoTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncMemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncMemoTagLocalEntity
import io.github.taetae98coding.diary.core.mapper.toDomain
import io.github.taetae98coding.diary.core.mapper.toLocal
import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.domain.memo.repository.AccountMemoRepository
import io.github.taetae98coding.diary.library.paging.common.mapPagingLatest
import kotlin.time.Clock
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class AccountMemoRepositoryImpl(
    @param:Provided
    private val clock: Clock,
    @param:Provided
    private val databaseTransactor: DatabaseTransactor,
    @param:Provided
    private val memoLocalDataSource: MemoLocalDataSource,
    @param:Provided
    private val memoTagLocalDataSource: MemoTagLocalDataSource,
    @param:Provided
    private val accountMemoLocalDataSource: AccountMemoLocalDataSource,
    @param:Provided
    private val syncMemoLocalDataSource: SyncMemoLocalDataSource,
    @param:Provided
    private val syncMemoTagLocalDataSource: SyncMemoTagLocalDataSource,
) : AccountMemoRepository {
    override suspend fun add(
        accountId: Uuid,
        detail: MemoDetail,
        primaryTag: Uuid?,
        memoTagIds: Set<Uuid>,
    ) {
        val memoId = Uuid.random()
        val now = clock.now().toEpochMilliseconds()

        databaseTransactor.writeTransaction {
            memoLocalDataSource.upsert(
                MemoLocalEntity(
                    id = memoId,
                    detail = detail.toLocal(),
                    primaryTag = primaryTag,
                    createdAt = now,
                    updatedAt = now,
                ),
            )
            memoTagLocalDataSource.upsert(
                memoTagIds.map { tagId ->
                    MemoTagLocalEntity(
                        memoId = memoId,
                        tagId = tagId,
                        isMemoTag = true,
                        updatedAt = clock.now().toEpochMilliseconds(),
                    )
                },
            )
            accountMemoLocalDataSource.upsert(
                AccountMemoLocalEntity(
                    accountId = accountId,
                    memoId = memoId,
                ),
            )
            syncMemoTagLocalDataSource.upsert(
                memoTagIds.map { tagId ->
                    SyncMemoTagLocalEntity(memoId = memoId, tagId = tagId)
                },
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
                updatedAt = clock.now().toEpochMilliseconds(),
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
                updatedAt = clock.now().toEpochMilliseconds(),
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
                updatedAt = clock.now().toEpochMilliseconds(),
            )
            syncMemoLocalDataSource.upsert(
                SyncMemoLocalEntity(memoId = memoId),
            )
        }
    }

    override fun pageByTag(
        accountId: Uuid,
        tagId: Uuid,
    ): Flow<PagingData<Memo>> {
        val pager = Pager(
            config = PagingConfig(pageSize = 30),
            pagingSourceFactory = { accountMemoLocalDataSource.pageByTag(accountId, tagId) },
        )

        return pager.flow.mapPagingLatest(MemoLocalEntity::toDomain)
    }
}
