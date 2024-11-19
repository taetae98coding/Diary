package io.github.taetae98coding.diary.domain.fcm.repository

public interface FCMRepository {
	public suspend fun upsert(token: String, owner: String)

	public suspend fun delete(token: String)
}
