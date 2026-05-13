package io.github.taetae98coding.diary.core.model.account

import kotlin.uuid.Uuid

public sealed interface Account {
    public val accountId: Uuid
    public val isAuthorized: Boolean

    public data object Guest : Account {
        override val accountId: Uuid = Uuid.NIL
        override val isAuthorized: Boolean = true
    }

    public data class User(
        val accountInfo: AccountInfo,
        val accountMetaData: AccountMetaData?,
    ) : Account {
        override val accountId: Uuid
            get() = accountInfo.id

        override val isAuthorized: Boolean
            get() = accountInfo.isAuthorized
    }
}
