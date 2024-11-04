package io.github.taetae98coding.diary.domain.account.usecase

import io.github.taetae98coding.diary.domain.account.repository.AccountRepository
import org.koin.core.annotation.Factory

@Factory
public class LogoutUseCase internal constructor(
    private val repository: AccountRepository,
) {
    public suspend operator fun invoke(): Result<Unit> {
        return runCatching { repository.clear() }
    }
}
