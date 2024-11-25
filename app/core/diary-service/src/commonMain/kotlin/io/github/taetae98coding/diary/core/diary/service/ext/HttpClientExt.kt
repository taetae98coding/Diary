package io.github.taetae98coding.diary.core.diary.service.ext

import io.github.taetae98coding.diary.common.exception.ApiException
import io.github.taetae98coding.diary.common.exception.account.AccountNotFoundException
import io.github.taetae98coding.diary.common.exception.account.ExistEmailException
import io.github.taetae98coding.diary.common.model.response.DiaryResponse
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess

internal suspend inline fun <reified T> HttpResponse.getOrThrow(): T =
	if (status.isSuccess()) {
		val body = body<DiaryResponse<T>>()

		requireNotNull(body.body)
	} else {
		val exception =
			when (val errorBody = body<DiaryResponse<Unit>>()) {
				DiaryResponse.AlreadyExistEmail -> ExistEmailException()
				DiaryResponse.AccountNotFound -> AccountNotFoundException()
				else -> ApiException(errorBody.message)
			}

		throw exception
	}
