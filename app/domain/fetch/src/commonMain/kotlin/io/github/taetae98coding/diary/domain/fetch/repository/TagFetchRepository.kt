package io.github.taetae98coding.diary.domain.fetch.repository

public interface TagFetchRepository {
	public suspend fun fetch(uid: String)
}
