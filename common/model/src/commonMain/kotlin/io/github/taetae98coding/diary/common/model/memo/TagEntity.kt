package io.github.taetae98coding.diary.common.model.memo

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class TagEntity(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("color")
    val color: Int,
    @SerialName("owner")
    val owner: String,
    @SerialName("isFinish")
    val isFinish: Boolean,
    @SerialName("isDelete")
    val isDelete: Boolean,
    @SerialName("updateAt")
    val updateAt: Instant,
)
