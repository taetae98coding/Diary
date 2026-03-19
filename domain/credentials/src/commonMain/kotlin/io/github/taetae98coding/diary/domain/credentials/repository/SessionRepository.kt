package io.github.taetae98coding.diary.domain.credentials.repository

public interface SessionRepository {
    public suspend fun updateByGoogleAuthorizationCode(
        clientId: String,
        code: String,
        redirectUri: String,
    )

    public suspend fun updateByGoogleIdToken(idToken: String)

    public suspend fun delete()
}
