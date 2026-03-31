package io.github.taetae98coding.diary.data.tag.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import io.github.taetae98coding.diary.core.database.api.DatabaseTransactor
import io.github.taetae98coding.diary.core.database.api.datasource.AccountTagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.SyncTagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.TagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.AccountTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.TagLocalEntity
import io.github.taetae98coding.diary.core.mapper.toDomain
import io.github.taetae98coding.diary.core.mapper.toLocal
import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.core.model.tag.TagDetail
import io.github.taetae98coding.diary.domain.tag.repository.AccountTagRepository
import io.github.taetae98coding.diary.library.paging.common.mapPagingLatest
import kotlin.time.Clock
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class AccountTagRepositoryImpl(
    @param:Provided
    private val databaseTransactor: DatabaseTransactor,
    @param:Provided
    private val tagLocalDataSource: TagLocalDataSource,
    @param:Provided
    private val accountTagLocalDataSource: AccountTagLocalDataSource,
    @param:Provided
    private val syncTagLocalDataSource: SyncTagLocalDataSource,
) : AccountTagRepository {
    override suspend fun add(
        accountId: Uuid,
        detail: TagDetail,
    ) {
        val tagId = Uuid.random()
        val now = Clock.System.now().toEpochMilliseconds()

        databaseTransactor.writeTransaction {
            tagLocalDataSource.upsert(
                TagLocalEntity(
                    id = tagId,
                    detail = detail.toLocal(),
                    createdAt = now,
                    updatedAt = now,
                ),
            )
            accountTagLocalDataSource.upsert(
                AccountTagLocalEntity(
                    accountId = accountId,
                    tagId = tagId,
                ),
            )
            syncTagLocalDataSource.upsert(
                SyncTagLocalEntity(tagId = tagId),
            )
        }
    }

    override suspend fun updateDetail(
        tagId: Uuid,
        detail: TagDetail,
    ) {
        databaseTransactor.writeTransaction {
            tagLocalDataSource.updateDetail(
                tagId = tagId,
                detail = detail.toLocal(),
                updatedAt = Clock.System.now().toEpochMilliseconds(),
            )
            syncTagLocalDataSource.upsert(
                SyncTagLocalEntity(tagId = tagId),
            )
        }
    }

    override suspend fun updateFinish(
        tagId: Uuid,
        isFinished: Boolean,
    ) {
        databaseTransactor.writeTransaction {
            tagLocalDataSource.updateFinish(
                tagId = tagId,
                isFinished = isFinished,
                updatedAt = Clock.System.now().toEpochMilliseconds(),
            )
            syncTagLocalDataSource.upsert(
                SyncTagLocalEntity(tagId = tagId),
            )
        }
    }

    override suspend fun updateDelete(
        tagId: Uuid,
        isDeleted: Boolean,
    ) {
        databaseTransactor.writeTransaction {
            tagLocalDataSource.updateDelete(
                tagId = tagId,
                isDeleted = isDeleted,
                updatedAt = Clock.System.now().toEpochMilliseconds(),
            )
            syncTagLocalDataSource.upsert(
                SyncTagLocalEntity(tagId = tagId),
            )
        }
    }

    override fun page(accountId: Uuid): Flow<PagingData<Tag>> {
        val pager = Pager(
            config = PagingConfig(pageSize = 30),
            pagingSourceFactory = { accountTagLocalDataSource.page(accountId) },
        )

        return pager.flow.mapPagingLatest(TagLocalEntity::toDomain)
    }
}
