package io.github.taetae98coding.diary.core.diary.service.ext

import io.github.taetae98coding.diary.common.exception.ApiException
import io.github.taetae98coding.diary.common.exception.account.AccountNotFoundException
import io.github.taetae98coding.diary.common.exception.account.ExistEmailException
import io.github.taetae98coding.diary.common.model.response.DiaryResponse
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess

internal suspend inline fun <reified T> HttpResponse.getOrThrow(): T = if (status.isSuccess()) {
	try {
		val body = body<DiaryResponse<T>>()

		requireNotNull(body.body)
	} catch (throwable: Throwable) {
		throw wrapApiException(throwable = throwable)
	}
} else {
	val errorBody = try {
		body<DiaryResponse<Unit>>()
	} catch (throwable: Throwable) {
		throw wrapApiException(throwable = throwable)
	}

	throw when (errorBody) {
		DiaryResponse.AlreadyExistEmail -> throw ExistEmailException()
		DiaryResponse.AccountNotFound -> throw AccountNotFoundException()
		else -> throw ApiException(message = errorBody.message)
	}
}

private fun wrapApiException(
	message: String = "Unknown error",
	throwable: Throwable,
): ApiException = ApiException(message = message, cause = throwable)
