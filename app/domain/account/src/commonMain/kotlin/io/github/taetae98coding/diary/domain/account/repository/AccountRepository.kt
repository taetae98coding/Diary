package io.github.taetae98coding.diary.domain.account.repository

import kotlinx.coroutines.flow.Flow

public interface AccountRepository {
	public fun getEmail(): Flow<String?>

	public fun getUid(): Flow<String?>
}
