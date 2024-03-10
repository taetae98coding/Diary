package com.taetae98.diary.domain.entity.account

public sealed class Account {
    public abstract val uid: String?
    public abstract val isLogin: Boolean

    public data object Guest : Account() {
        override val uid: String? = null
        override val isLogin: Boolean = false
    }

    public data class Member(
        override val uid: String,
        val email: String?
    ) : Account() {
        override val isLogin: Boolean = true
    }
}