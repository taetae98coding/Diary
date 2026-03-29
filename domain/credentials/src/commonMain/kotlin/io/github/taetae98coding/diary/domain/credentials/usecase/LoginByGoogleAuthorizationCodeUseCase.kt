package io.github.taetae98coding.diary.domain.credentials.usecase

import io.github.taetae98coding.diary.domain.credentials.repository.SessionRepository
import io.github.taetae98coding.diary.domain.sync.usecase.RequestSyncUseCase
import org.koin.core.annotation.Factory

@Factory
public class LoginByGoogleAuthorizationCodeUseCase(
    private val sessionRepository: SessionRepository,
    private val requestSyncUseCase: RequestSyncUseCase,
) {
    public suspend operator fun invoke(
        clientId: String,
        code: String,
        redirectUri: String,
    ): Result<Unit> {
        return runCatching {
            sessionRepository.updateByGoogleAuthorizationCode(clientId, code, redirectUri)
            requestSyncUseCase()
        }
    }
}
