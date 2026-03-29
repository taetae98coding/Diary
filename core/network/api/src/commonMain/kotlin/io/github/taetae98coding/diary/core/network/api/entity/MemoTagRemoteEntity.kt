package io.github.taetae98coding.diary.core.network.api.entity

import kotlin.uuid.Uuid
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class MemoTagRemoteEntity(
    @SerialName("memoId")
    val memoId: Uuid,
    @SerialName("tagId")
    val tagId: Uuid,
    @SerialName("isMemoTag")
    val isMemoTag: Boolean,
    @SerialName("updatedAt")
    val updatedAt: Long,
)
