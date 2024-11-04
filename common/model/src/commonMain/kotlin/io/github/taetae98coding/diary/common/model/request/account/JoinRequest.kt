package io.github.taetae98coding.diary.common.model.request.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class JoinRequest(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String,
)
