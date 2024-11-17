package io.github.taetae98coding.diary.common.model.memo

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class MemoTagEntity(
    @SerialName("memoId")
    val memoId: String,
    @SerialName("tagId")
    val tagId: String,
    @SerialName("isSelected")
    val isSelected: Boolean,
    @SerialName("updateAt")
    val updateAt: Instant
)
