package io.github.taetae98coding.diary.data.sync

import io.github.taetae98coding.diary.core.database.api.DatabaseTransactor
import io.github.taetae98coding.diary.core.database.api.datasource.AccountRoutineLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.RoutineLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.SyncRoutineLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.AccountRoutineLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncRoutineLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncStateLocalEntity
import io.github.taetae98coding.diary.core.mapper.toLocal
import io.github.taetae98coding.diary.core.network.api.datasource.RoutineRemoteDataSource
import io.github.taetae98coding.diary.core.network.api.entity.RoutineRemoteEntity
import kotlin.uuid.Uuid
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
internal class RoutineDownloader(
    @Provided
    databaseTransactor: DatabaseTransactor,
    @param:Provided
    private val routineLocalDataSource: RoutineLocalDataSource,
    @param:Provided
    private val accountRoutineLocalDataSource: AccountRoutineLocalDataSource,
    @param:Provided
    private val syncRoutineLocalDataSource: SyncRoutineLocalDataSource,
    @param:Provided
    private val routineRemoteDataSource: RoutineRemoteDataSource,
) : Downloader(databaseTransactor) {
    override suspend fun download(accountId: Uuid) {
        pull(
            getLastUpdatedAt = { syncRoutineLocalDataSource.getLastUpdatedAt(accountId, SyncStateLocalEntity.UP_TO_DATE) },
            pullRemote = routineRemoteDataSource::pull,
            save = { list ->
                routineLocalDataSource.upsert(list.map(RoutineRemoteEntity::toLocal))
                accountRoutineLocalDataSource.upsert(list.map { AccountRoutineLocalEntity(accountId, it.id) })
                syncRoutineLocalDataSource.upsert(list.map { SyncRoutineLocalEntity(routineId = it.id, state = SyncStateLocalEntity.UP_TO_DATE) })
            },
        )
    }
}
