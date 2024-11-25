package io.github.taetae98coding.diary.domain.backup.usecase

import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.backup.repository.MemoBackupRepository
import io.github.taetae98coding.diary.domain.backup.repository.TagBackupRepository
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class BackupUseCase internal constructor(private val getAccountUseCase: GetAccountUseCase, private val tagBackupRepository: TagBackupRepository, private val memoBackupRepository: MemoBackupRepository) {
	public suspend operator fun invoke(): Result<Unit> =
		runCatching {
			val account = getAccountUseCase().first().getOrThrow()
			if (account is Account.Member) {
				tagBackupRepository.backup(account.uid)
				memoBackupRepository.backup(account.uid)
			}
		}
}
