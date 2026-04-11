package io.github.taetae98coding.diary.data.memo.repository

import io.github.taetae98coding.diary.core.database.api.DatabaseTransactor
import io.github.taetae98coding.diary.core.database.api.datasource.MemoLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.MemoTagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.SyncMemoLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.SyncMemoTagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.MemoTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncMemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncMemoTagLocalEntity
import io.github.taetae98coding.diary.domain.memo.repository.AccountMemoTagRepository
import kotlin.time.Clock
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class AccountMemoTagRepositoryImpl(
    @param:Provided
    private val clock: Clock,
    @param:Provided
    private val databaseTransactor: DatabaseTransactor,
    @param:Provided
    private val memoLocalDataSource: MemoLocalDataSource,
    @param:Provided
    private val memoTagLocalDataSource: MemoTagLocalDataSource,
    @param:Provided
    private val syncMemoLocalDataSource: SyncMemoLocalDataSource,
    @param:Provided
    private val syncMemoTagLocalDataSource: SyncMemoTagLocalDataSource,
) : AccountMemoTagRepository {
    override suspend fun updatePrimaryTag(
        memoId: Uuid,
        primaryTag: Uuid?,
    ) {
        val now = clock.now().toEpochMilliseconds()

        databaseTransactor.writeTransaction {
            memoLocalDataSource.updatePrimaryTag(
                memoId = memoId,
                primaryTag = primaryTag,
                updatedAt = now,
            )
            if (primaryTag != null) {
                memoTagLocalDataSource.upsert(
                    listOf(
                        MemoTagLocalEntity(
                            memoId = memoId,
                            tagId = primaryTag,
                            isMemoTag = true,
                            updatedAt = now,
                        ),
                    ),
                )
                syncMemoTagLocalDataSource.upsert(
                    SyncMemoTagLocalEntity(memoId = memoId, tagId = primaryTag),
                )
            }
            syncMemoLocalDataSource.upsert(
                SyncMemoLocalEntity(memoId = memoId),
            )
        }
    }

    override suspend fun upsertMemoTag(
        memoId: Uuid,
        tagId: Uuid,
        isMemoTag: Boolean,
    ) {
        val now = clock.now().toEpochMilliseconds()

        databaseTransactor.writeTransaction {
            memoTagLocalDataSource.upsert(
                listOf(
                    MemoTagLocalEntity(
                        memoId = memoId,
                        tagId = tagId,
                        isMemoTag = isMemoTag,
                        updatedAt = now,
                    ),
                ),
            )
            if (!isMemoTag) {
                val memo = memoLocalDataSource.get(memoId).first()
                if (memo?.primaryTag == tagId) {
                    memoLocalDataSource.updatePrimaryTag(
                        memoId = memoId,
                        primaryTag = null,
                        updatedAt = now,
                    )
                    syncMemoLocalDataSource.upsert(
                        SyncMemoLocalEntity(memoId = memoId),
                    )
                }
            }
            syncMemoTagLocalDataSource.upsert(
                SyncMemoTagLocalEntity(memoId = memoId, tagId = tagId),
            )
        }
    }
}
