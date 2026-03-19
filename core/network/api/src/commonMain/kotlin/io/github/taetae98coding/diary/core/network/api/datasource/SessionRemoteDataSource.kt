package io.github.taetae98coding.diary.core.network.api.datasource

import io.github.taetae98coding.diary.core.network.api.entity.SessionRemoteEntity

public interface SessionRemoteDataSource {
    public suspend fun getByGoogleAuthorizationCode(
        clientId: String,
        code: String,
        redirectUri: String,
    ): SessionRemoteEntity

    public suspend fun getByGoogleIdToken(idToken: String): SessionRemoteEntity
}
