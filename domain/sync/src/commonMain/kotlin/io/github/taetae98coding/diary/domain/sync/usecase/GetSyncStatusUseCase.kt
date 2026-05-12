@file:OptIn(ExperimentalCoroutinesApi::class)

package io.github.taetae98coding.diary.domain.sync.usecase

import io.github.taetae98coding.diary.core.model.sync.SyncStatus
import io.github.taetae98coding.diary.domain.sync.manager.SyncManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class GetSyncStatusUseCase(
    @param:Provided
    private val syncManager: SyncManager,
) {
    public operator fun invoke(): Flow<Result<SyncStatus>> {
        return flow {
            emitAll(syncManager.syncStatus)
        }.mapLatest {
            Result.success(it)
        }.catch {
            emit(Result.failure(it))
        }
    }
}
