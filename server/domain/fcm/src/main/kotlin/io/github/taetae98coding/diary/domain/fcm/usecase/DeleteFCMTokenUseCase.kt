package io.github.taetae98coding.diary.domain.fcm.usecase

import io.github.taetae98coding.diary.domain.fcm.repository.FCMRepository
import org.koin.core.annotation.Factory

@Factory
public class DeleteFCMTokenUseCase internal constructor(private val repository: FCMRepository) {
	public suspend operator fun invoke(token: String): Result<Unit> = runCatching { repository.delete(token) }
}
