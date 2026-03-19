package io.github.taetae98coding.diary.core.google.credentials.api

public interface GoogleCredentialsManager {
    public suspend fun get(): GoogleCredentials
}
