package io.github.taetae98coding.diary.data.sync

import io.github.taetae98coding.diary.core.database.api.DatabaseTransactor
import io.github.taetae98coding.diary.core.database.api.datasource.AccountTagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.SyncTagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.TagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.AccountTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncStateLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncTagLocalEntity
import io.github.taetae98coding.diary.core.mapper.toLocal
import io.github.taetae98coding.diary.core.network.api.datasource.TagRemoteDataSource
import io.github.taetae98coding.diary.core.network.api.entity.TagRemoteEntity
import kotlin.uuid.Uuid
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
internal class TagDownloader(
    @Provided
    databaseTransactor: DatabaseTransactor,
    @param:Provided
    private val tagLocalDataSource: TagLocalDataSource,
    @param:Provided
    private val accountTagLocalDataSource: AccountTagLocalDataSource,
    @param:Provided
    private val syncTagLocalDataSource: SyncTagLocalDataSource,
    @param:Provided
    private val tagRemoteDataSource: TagRemoteDataSource,
) : Downloader(databaseTransactor) {
    override suspend fun download(accountId: Uuid) {
        pull(
            getLastUpdatedAt = { syncTagLocalDataSource.getLastUpdatedAt(accountId, SyncStateLocalEntity.UP_TO_DATE) },
            pullRemote = tagRemoteDataSource::pull,
            save = { list ->
                tagLocalDataSource.upsert(list.map(TagRemoteEntity::toLocal))
                accountTagLocalDataSource.upsert(list.map { AccountTagLocalEntity(accountId, it.id) })
                syncTagLocalDataSource.upsert(list.map { SyncTagLocalEntity(tagId = it.id, state = SyncStateLocalEntity.UP_TO_DATE) })
            },
        )
    }
}
