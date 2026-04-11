package io.github.taetae98coding.diary.data.sync

import io.github.taetae98coding.diary.core.database.api.datasource.SyncTagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.SyncStateLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.TagLocalEntity
import io.github.taetae98coding.diary.core.mapper.toRemote
import io.github.taetae98coding.diary.core.network.api.datasource.TagRemoteDataSource
import kotlin.uuid.Uuid
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
internal class TagUploader(
    @param:Provided
    private val syncTagLocalDataSource: SyncTagLocalDataSource,
    @param:Provided
    private val tagRemoteDataSource: TagRemoteDataSource,
) : Uploader() {
    override suspend fun upload(accountId: Uuid) {
        push(
            getPending = { syncTagLocalDataSource.getBySyncState(accountId, SyncStateLocalEntity.PENDING) },
            toRemote = TagLocalEntity::toRemote,
            pushRemote = tagRemoteDataSource::push,
            markSynced = { list -> syncTagLocalDataSource.upsert(list.map { SyncTagLocalEntity(it.id, SyncStateLocalEntity.SYNCING) }) },
        )
    }
}
