package io.github.taetae98coding.diary.core.model.routine

public class RoutineTitleBlankException(
    override val message: String? = "Routine title must not be blank",
    override val cause: Throwable? = null,
) : Exception(message, cause)
