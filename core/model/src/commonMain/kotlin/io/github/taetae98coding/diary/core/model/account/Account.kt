package io.github.taetae98coding.diary.core.model.account

import kotlin.uuid.Uuid

public sealed class Account {
    public abstract val accountId: Uuid

    public data object Guest : Account() {
        override val accountId: Uuid = Uuid.NIL
    }

    public data class User(
        override val accountId: Uuid,
        val email: String,
        val profileImage: String?,
    ) : Account()
}
