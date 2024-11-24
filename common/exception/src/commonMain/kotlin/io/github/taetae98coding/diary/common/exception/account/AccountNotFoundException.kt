package io.github.taetae98coding.diary.common.exception.account

public class AccountNotFoundException(override val message: String? = null, override val cause: Throwable? = null) : Exception(message, cause)
