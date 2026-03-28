package io.github.taetae98coding.diary.core.navigation

import androidx.navigation3.runtime.NavKey
import io.github.taetae98coding.diary.core.navigation.argument.TagId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class TagDetailNavKey(
    @SerialName("tagId")
    val tagId: TagId,
) : NavKey
