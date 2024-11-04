package io.github.taetae98coding.diary.domain.fetch.repository

public interface FetchRepository {
    public suspend fun fetchMemo(uid: String)
}
