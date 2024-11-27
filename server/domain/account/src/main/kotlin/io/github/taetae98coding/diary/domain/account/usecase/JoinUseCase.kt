package io.github.taetae98coding.diary.domain.account.usecase

import io.github.taetae98coding.diary.common.exception.account.ExistEmailException
import io.github.taetae98coding.diary.common.exception.account.InvalidEmailException
import io.github.taetae98coding.diary.core.model.Account
import io.github.taetae98coding.diary.domain.account.repository.AccountRepository
import io.github.taetae98coding.diary.library.kotlin.regex.email
import org.koin.core.annotation.Factory
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Factory
public class JoinUseCase internal constructor(
	private val hashingPasswordUseCase: HashingPasswordUseCase,
	private val repository: AccountRepository,
) {
	public suspend operator fun invoke(email: String, password: String): Result<Unit> =
		runCatching {
			// TODO password check

			if (!email.contains(Regex.email())) throw InvalidEmailException()
			if (repository.contains(email)) throw ExistEmailException()

			val uid = Uuid.random().toString()

			val account =
				Account(
					uid = uid,
					email = email,
					password = hashingPasswordUseCase(email, password).getOrThrow(),
				)

			repository.upsert(account)
		}
}
