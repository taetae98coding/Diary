package com.taetae98.diary.domain.entity.account.account

public sealed class Credential {
    public data class Google(
        val idToken: String,
        val accessToken: String?,
    ) : Credential()
}