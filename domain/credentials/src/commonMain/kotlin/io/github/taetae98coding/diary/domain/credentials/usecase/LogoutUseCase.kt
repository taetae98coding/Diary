package io.github.taetae98coding.diary.domain.credentials.usecase

import io.github.taetae98coding.diary.domain.credentials.repository.SessionRepository
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class LogoutUseCase(
    @param:Provided
    private val sessionRepository: SessionRepository,
) {
    public suspend operator fun invoke(): Result<Unit> {
        return runCatching {
            sessionRepository.delete()
        }
    }
}
