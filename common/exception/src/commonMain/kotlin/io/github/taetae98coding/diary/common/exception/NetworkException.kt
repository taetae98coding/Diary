package io.github.taetae98coding.diary.common.exception

public class NetworkException(
	override val message: String? = null,
	override val cause: Throwable? = null,
) : Exception(message, cause)
