package io.github.taetae98coding.diary.domain.credentials.usecase

import io.github.taetae98coding.diary.domain.credentials.repository.SessionRepository
import org.koin.core.annotation.Factory

@Factory
public class LoginByGoogleIdTokenUseCase(private val sessionRepository: SessionRepository) {
    public suspend operator fun invoke(idToken: String): Result<Unit> {
        return runCatching {
            sessionRepository.updateByGoogleIdToken(idToken)
        }
    }
}
