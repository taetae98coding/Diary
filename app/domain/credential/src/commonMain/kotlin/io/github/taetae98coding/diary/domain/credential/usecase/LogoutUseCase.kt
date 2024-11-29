package io.github.taetae98coding.diary.domain.credential.usecase

import io.github.taetae98coding.diary.domain.backup.usecase.BackupUseCase
import io.github.taetae98coding.diary.domain.credential.repository.CredentialRepository
import io.github.taetae98coding.diary.domain.fcm.usecase.UpdateFCMTokenUseCase
import org.koin.core.annotation.Factory

@Factory
public class LogoutUseCase internal constructor(
	private val backupUseCase: BackupUseCase,
	private val updateFCMTokenUseCase: UpdateFCMTokenUseCase,
	private val repository: CredentialRepository,
) {
	public suspend operator fun invoke(): Result<Unit> =
		runCatching {
			backupUseCase()
			repository.clear()
			updateFCMTokenUseCase()
		}
}
