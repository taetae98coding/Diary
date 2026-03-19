package io.github.taetae98coding.diary.core.network.api.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SessionRemoteEntity(
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("refreshToken")
    val refreshToken: String,
    @SerialName("account")
    val account: AccountRemoteEntity,
)
