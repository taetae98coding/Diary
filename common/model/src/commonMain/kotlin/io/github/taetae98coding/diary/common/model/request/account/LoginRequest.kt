package io.github.taetae98coding.diary.common.model.request.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class LoginRequest(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String,
)
