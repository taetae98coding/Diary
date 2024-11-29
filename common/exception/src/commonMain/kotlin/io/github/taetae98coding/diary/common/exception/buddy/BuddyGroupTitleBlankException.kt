package io.github.taetae98coding.diary.common.exception.buddy

public class BuddyGroupTitleBlankException(
	override val message: String? = null,
	override val cause: Throwable? = null,
) : Exception(message, cause)
