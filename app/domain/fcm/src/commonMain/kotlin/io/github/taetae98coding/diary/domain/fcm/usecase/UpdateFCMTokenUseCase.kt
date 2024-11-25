package io.github.taetae98coding.diary.domain.fcm.usecase

import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.fcm.repository.FCMRepository
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class UpdateFCMTokenUseCase internal constructor(private val getAccountUseCase: GetAccountUseCase, private val repository: FCMRepository) {
	public suspend operator fun invoke(): Result<Unit> =
		runCatching {
			val account = getAccountUseCase().first().getOrThrow()
			if (account is Account.Member) {
				repository.upsert()
			} else {
				repository.delete()
			}
		}
}
