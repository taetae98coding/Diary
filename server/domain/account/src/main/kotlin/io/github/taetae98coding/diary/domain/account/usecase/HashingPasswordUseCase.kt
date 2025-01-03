package io.github.taetae98coding.diary.domain.account.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory
import java.security.MessageDigest

@OptIn(ExperimentalStdlibApi::class)
@Factory
public class HashingPasswordUseCase internal constructor() {
	public suspend operator fun invoke(email: String, password: String): Result<String> =
		runCatching {
			withContext(Dispatchers.IO) {
				MessageDigest
					.getInstance("SHA-256")
					.apply { update((email + password).toByteArray()) }
					.digest()
					.toHexString()
			}
		}
}
