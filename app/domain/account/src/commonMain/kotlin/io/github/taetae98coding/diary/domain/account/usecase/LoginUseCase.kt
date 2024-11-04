package io.github.taetae98coding.diary.domain.account.usecase

import io.github.taetae98coding.diary.domain.account.repository.AccountRepository
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class LoginUseCase internal constructor(
    private val repository: AccountRepository,
) {
    public suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return runCatching {
            val token = repository.fetchToken(email, password).first()

            repository.save(email = email, token = token)
        }
    }
}
