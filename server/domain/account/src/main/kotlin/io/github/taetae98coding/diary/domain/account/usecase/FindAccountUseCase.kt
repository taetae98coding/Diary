package io.github.taetae98coding.diary.domain.account.usecase

import io.github.taetae98coding.diary.core.model.Account
import io.github.taetae98coding.diary.domain.account.repository.AccountRepository
import org.koin.core.annotation.Factory

@Factory
public class FindAccountUseCase internal constructor(private val hashingPasswordUseCase: HashingPasswordUseCase, private val repository: AccountRepository) {
	public suspend operator fun invoke(email: String, password: String): Result<Account?> =
		runCatching {
			repository.findByEmail(
				email = email,
				password = hashingPasswordUseCase(email, password).getOrThrow(),
			)
		}
}
