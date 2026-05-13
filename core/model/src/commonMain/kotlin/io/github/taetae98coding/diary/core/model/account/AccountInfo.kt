package io.github.taetae98coding.diary.core.model.account

import kotlin.uuid.Uuid

public data class AccountInfo(
    val id: Uuid,
    val email: String,
    val isAuthorized: Boolean,
)
