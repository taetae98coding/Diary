package io.github.taetae98coding.diary.domain.account.usecase

import io.github.taetae98coding.diary.core.model.Account
import io.github.taetae98coding.diary.domain.account.repository.AccountRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
public class FindAccountUseCase internal constructor(
	private val hashingPasswordUseCase: HashingPasswordUseCase,
	private val repository: AccountRepository,
) {
	public operator fun invoke(email: String, password: String): Flow<Result<Account?>> = flow {
		repository
			.findByEmail(
				email = email,
				password = hashingPasswordUseCase(email, password).getOrThrow(),
			).also {
				emitAll(it)
			}
	}.mapLatest {
		Result.success(it)
	}.catch {
		emit(Result.failure(it))
	}
}
