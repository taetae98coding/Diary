package io.github.taetae98coding.diary.core.database.api

public interface DatabaseTransactor {
    public suspend fun <R> writeTransaction(block: suspend () -> R): R
}
