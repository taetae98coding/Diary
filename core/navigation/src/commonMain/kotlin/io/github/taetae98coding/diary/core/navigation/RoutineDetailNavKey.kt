package io.github.taetae98coding.diary.core.navigation

import androidx.navigation3.runtime.NavKey
import io.github.taetae98coding.diary.core.navigation.argument.RoutineId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class RoutineDetailNavKey(
    @SerialName("routineId")
    val routineId: RoutineId,
) : NavKey
