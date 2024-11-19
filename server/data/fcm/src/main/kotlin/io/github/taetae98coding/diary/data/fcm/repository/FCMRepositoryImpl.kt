package io.github.taetae98coding.diary.data.fcm.repository

import io.github.taetae98coding.diary.core.database.FCMTokenTable
import io.github.taetae98coding.diary.domain.fcm.repository.FCMRepository
import org.koin.core.annotation.Factory

@Factory
internal class FCMRepositoryImpl : FCMRepository {
	override suspend fun upsert(token: String, owner: String) {
		FCMTokenTable.upsert(token, owner)
	}

	override suspend fun delete(token: String) {
		FCMTokenTable.delete(token)
	}
}
