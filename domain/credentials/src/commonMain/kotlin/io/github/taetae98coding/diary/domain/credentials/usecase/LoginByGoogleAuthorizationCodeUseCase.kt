package io.github.taetae98coding.diary.domain.credentials.usecase

import io.github.taetae98coding.diary.core.model.sync.SyncType
import io.github.taetae98coding.diary.domain.credentials.repository.SessionRepository
import io.github.taetae98coding.diary.domain.sync.usecase.RequestSyncUseCase
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class LoginByGoogleAuthorizationCodeUseCase(
    private val requestSyncUseCase: RequestSyncUseCase,
    @param:Provided
    private val sessionRepository: SessionRepository,
) {
    public suspend operator fun invoke(
        clientId: String,
        code: String,
        redirectUri: String,
    ): Result<Unit> {
        return runCatching {
            sessionRepository.updateByGoogleAuthorizationCode(clientId, code, redirectUri)
            requestSyncUseCase(SyncType.Foreground)
        }
    }
}
