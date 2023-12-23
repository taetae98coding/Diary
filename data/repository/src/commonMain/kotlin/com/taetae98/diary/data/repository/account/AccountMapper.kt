package com.taetae98.diary.data.repository.account

import com.taetae98.diary.core.auth.api.AccountEntity
import com.taetae98.diary.core.auth.api.CredentialEntity
import com.taetae98.diary.domain.entity.account.account.Account
import com.taetae98.diary.domain.entity.account.account.Credential

internal fun Credential.toEntity(): CredentialEntity {
    return when (this) {
        is Credential.Google -> CredentialEntity.Google(
            idToken = idToken,
            accessToken = accessToken,
        )
    }
}

internal fun AccountEntity.toDomain(): Account {
    return when(this) {
        AccountEntity.Guest -> Account.Guest
        is AccountEntity.Member -> Account.Member(
            uid = uid,
            email = email
        )
    }
}