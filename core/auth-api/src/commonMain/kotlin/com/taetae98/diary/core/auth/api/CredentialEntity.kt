package com.taetae98.diary.core.auth.api

public sealed class CredentialEntity {
    public data class Google(
        val idToken: String,
        val accessToken: String?
    ) : CredentialEntity()
}