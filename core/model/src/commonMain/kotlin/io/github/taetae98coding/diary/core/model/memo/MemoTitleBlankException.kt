package io.github.taetae98coding.diary.core.model.memo

public class MemoTitleBlankException(
    override val message: String? = "Memo title must not be blank",
    override val cause: Throwable? = null,
) : Exception(message, cause)
