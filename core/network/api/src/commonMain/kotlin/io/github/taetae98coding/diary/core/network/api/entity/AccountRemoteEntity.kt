package io.github.taetae98coding.diary.core.network.api.entity

import kotlin.uuid.Uuid
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class AccountRemoteEntity(
    @SerialName("id")
    val id: Uuid,
    @SerialName("email")
    val email: String,
    @SerialName("profileImage")
    val profileImage: String?,
)
