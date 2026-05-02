package io.github.taetae98coding.diary.core.google.credentials.api

public sealed interface GoogleCredentials {
    public data class IdToken(val idToken: String) : GoogleCredentials
    public data class AuthorizationCode(
        val clientId: String,
        val code: String,
        val redirectUri: String,
    ) : GoogleCredentials
}
