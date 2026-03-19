package io.github.taetae98coding.diary.domain.sync.usecase

import io.github.taetae98coding.diary.domain.sync.repository.SyncRepository
import kotlin.uuid.Uuid
import org.koin.core.annotation.Factory

@Factory
public class RequestSyncUseCase(private val syncRepository: SyncRepository) {
    public operator fun invoke(accountId: Uuid) {
        syncRepository.requestSync(accountId)
    }
}
