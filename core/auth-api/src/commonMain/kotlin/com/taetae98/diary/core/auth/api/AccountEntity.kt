package com.taetae98.diary.core.auth.api

public sealed class AccountEntity {
    public abstract val uid: String?

    public data object Guest : AccountEntity() {
        override val uid: String? = null
    }

    public data class Member(
        override val uid: String,
        val email: String?
    ) : AccountEntity()
}