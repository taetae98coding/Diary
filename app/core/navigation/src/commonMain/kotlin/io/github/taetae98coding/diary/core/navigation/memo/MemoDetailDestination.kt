package io.github.taetae98coding.diary.core.navigation.memo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class MemoDetailDestination(
    @SerialName("memoId")
    val memoId: String,
)
