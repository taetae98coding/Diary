package io.github.taetae98coding.diary.core.account.preferences

import kotlinx.coroutines.flow.Flow

public interface AccountPreferences {
	public suspend fun save(email: String, uid: String, token: String)

	public suspend fun clear()

	public fun getEmail(): Flow<String?>

	public fun getUid(): Flow<String?>

	public fun getToken(): Flow<String?>
}
