package io.github.taetae98coding.diary.core.model.routine

public class RoutineRRulesEmptyException(
    override val message: String? = "Routine rrules must not be empty",
    override val cause: Throwable? = null,
) : Exception(message, cause)
