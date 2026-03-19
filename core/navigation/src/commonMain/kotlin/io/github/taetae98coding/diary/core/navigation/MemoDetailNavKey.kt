package io.github.taetae98coding.diary.core.navigation

import androidx.navigation3.runtime.NavKey
import io.github.taetae98coding.diary.core.navigation.argument.MemoId
import kotlin.uuid.Uuid
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class MemoDetailNavKey(
    @SerialName("memoId")
    val memoId: MemoId,
) : NavKey
