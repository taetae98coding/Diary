package com.taetae98.diary.domain.entity.account

public sealed class Account {
    public abstract val uid: String?

    public data object Guest : Account() {
        override val uid: String? = null
    }

    public data class Member(
        override val uid: String,
        val email: String?
    ) : Account()
}