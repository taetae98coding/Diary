package io.github.taetae98coding.diary.domain.fetch.repository

public interface MemoFetchRepository {
	public suspend fun fetch(uid: String)
}
