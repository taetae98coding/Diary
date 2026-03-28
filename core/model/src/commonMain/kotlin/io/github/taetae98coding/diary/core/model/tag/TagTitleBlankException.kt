package io.github.taetae98coding.diary.core.model.tag

public class TagTitleBlankException(
    override val message: String? = "Tag title must not be blank",
    override val cause: Throwable? = null,
) : Exception(message, cause)
