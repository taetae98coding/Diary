package io.github.taetae98coding.diary.domain.account.usecase

import io.github.taetae98coding.diary.common.exception.account.InvalidEmailException
import io.github.taetae98coding.diary.domain.account.repository.AccountRepository
import io.github.taetae98coding.diary.library.kotlin.regex.email
import org.koin.core.annotation.Factory

@Factory
public class JoinUseCase internal constructor(
    private val repository: AccountRepository,
) {
    public suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return runCatching {
            if (!email.contains(Regex.email())) throw InvalidEmailException()

            repository.join(email, password)
        }
    }
}
