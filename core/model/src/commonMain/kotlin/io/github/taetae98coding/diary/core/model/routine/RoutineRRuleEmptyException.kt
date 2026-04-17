package io.github.taetae98coding.diary.core.model.routine

public class RoutineRRuleEmptyException(
    override val message: String? = "Routine must have at least one RRule",
    override val cause: Throwable? = null,
) : Exception(message, cause)
