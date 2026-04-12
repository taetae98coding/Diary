package io.github.taetae98coding.diary.core.navigation.argument

import kotlin.jvm.JvmInline
import kotlin.uuid.Uuid
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class RoutineId(public val value: Uuid)
