package io.github.taetae98coding.diary.common.exception.account

public class InvalidEmailException(
	override val message: String? = "",
	override val cause: Throwable? = null,
) : Exception(message, cause)
