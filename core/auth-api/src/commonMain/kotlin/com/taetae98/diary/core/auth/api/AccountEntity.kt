package com.taetae98.diary.core.auth.api

public sealed class AccountEntity {
    public data object Guest : AccountEntity()

    public data class Member(
        val uid: String,
        val email: String?
    ) : AccountEntity()
}