package io.github.taetae98coding.diary.core.network.api.entity

import kotlin.uuid.Uuid
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class MemoRemoteEntity(
    @SerialName("id")
    val id: Uuid,
    @SerialName("detail")
    val detail: MemoDetailRemoteEntity,
    @SerialName("isFinished")
    val isFinished: Boolean,
    @SerialName("isDeleted")
    val isDeleted: Boolean,
    @SerialName("updatedAt")
    val updatedAt: Long,
    @SerialName("createdAt")
    val createdAt: Long,
)
