package io.github.taetae98coding.diary.domain.fcm.repository

public interface FCMRepository {
	public suspend fun upsert()

	public suspend fun delete()
}
