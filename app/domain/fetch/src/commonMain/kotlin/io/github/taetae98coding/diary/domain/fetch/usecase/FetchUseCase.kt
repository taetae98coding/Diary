package io.github.taetae98coding.diary.domain.fetch.usecase

import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.backup.usecase.BackupUseCase
import io.github.taetae98coding.diary.domain.fetch.repository.MemoFetchRepository
import io.github.taetae98coding.diary.domain.fetch.repository.TagFetchRepository
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class FetchUseCase internal constructor(private val getAccountUseCase: GetAccountUseCase, private val backupUseCase: BackupUseCase, private val memoFetchRepository: MemoFetchRepository, private val tagFetchRepository: TagFetchRepository) {
	public suspend operator fun invoke(): Result<Unit> =
		runCatching {
			val account = getAccountUseCase().first().getOrThrow()
			if (account is Account.Member) {
				backupUseCase()
				tagFetchRepository.fetch(account.uid)
				memoFetchRepository.fetch(account.uid)
			}
		}
}
