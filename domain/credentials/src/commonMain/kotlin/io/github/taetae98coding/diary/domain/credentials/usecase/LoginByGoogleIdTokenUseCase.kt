package io.github.taetae98coding.diary.domain.credentials.usecase

import io.github.taetae98coding.diary.core.model.sync.SyncType
import io.github.taetae98coding.diary.domain.credentials.repository.SessionRepository
import io.github.taetae98coding.diary.domain.sync.usecase.RequestSyncUseCase
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class LoginByGoogleIdTokenUseCase(
    private val requestSyncUseCase: RequestSyncUseCase,
    @param:Provided
    private val sessionRepository: SessionRepository,
) {
    public suspend operator fun invoke(idToken: String): Result<Unit> {
        return runCatching {
            sessionRepository.updateByGoogleIdToken(idToken)
            requestSyncUseCase(SyncType.Foreground)
        }
    }
}
