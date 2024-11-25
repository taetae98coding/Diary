package io.github.taetae98coding.diary.domain.fcm.usecase

import io.github.taetae98coding.diary.domain.fcm.repository.FCMRepository
import org.koin.core.annotation.Factory

@Factory
public class UpsertFCMTokenUseCase internal constructor(private val repository: FCMRepository) {
	public suspend operator fun invoke(token: String, owner: String): Result<Unit> = runCatching { repository.upsert(token, owner) }
}
