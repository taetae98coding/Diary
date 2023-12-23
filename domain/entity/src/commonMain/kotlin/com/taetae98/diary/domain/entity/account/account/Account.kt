package com.taetae98.diary.domain.entity.account.account

public sealed class Account {
    public data object Guest : Account()

    public data class Member(
        val uid: String,
        val email: String?
    ) : Account()
}