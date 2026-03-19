package io.github.taetae98coding.diary.domain.credentials.usecase

import io.github.taetae98coding.diary.domain.credentials.repository.SessionRepository
import org.koin.core.annotation.Factory

@Factory
public class LogoutUseCase(private val sessionRepository: SessionRepository) {
    public suspend operator fun invoke(): Result<Unit> {
        return runCatching {
            sessionRepository.delete()
        }
    }
}
