package io.github.taetae98coding.diary.domain.sync.usecase

import io.github.taetae98coding.diary.core.model.sync.SyncStatus
import io.github.taetae98coding.diary.domain.sync.SyncManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory

@Factory
public class GetSyncStatusUseCase(private val syncManager: SyncManager) {
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
