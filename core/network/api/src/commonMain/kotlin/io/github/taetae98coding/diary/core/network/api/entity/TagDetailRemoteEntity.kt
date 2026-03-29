package io.github.taetae98coding.diary.core.network.api.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class TagDetailRemoteEntity(
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("color")
    val color: Int,
)
