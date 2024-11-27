package io.github.taetae98coding.diary.domain.credential.usecase

import io.github.taetae98coding.diary.domain.credential.repository.CredentialRepository
import io.github.taetae98coding.diary.domain.fcm.usecase.UpdateFCMTokenUseCase
import io.github.taetae98coding.diary.domain.fetch.usecase.FetchUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
public class LoginUseCase internal constructor(
	private val coroutineScope: CoroutineScope,
	private val repository: CredentialRepository,
	private val fetchUseCase: FetchUseCase,
	private val updateFCMTokenUseCase: UpdateFCMTokenUseCase,
) {
	public suspend operator fun invoke(email: String, password: String): Result<Unit> =
		runCatching {
			val token = repository.fetchToken(email, password).first()
			repository.save(email = email, token = token)

			coroutineScope.launch {
				listOf(
					async { fetchUseCase() },
					async { updateFCMTokenUseCase() },
				).awaitAll()
			}
		}
}
