package io.github.taetae98coding.diary.core.diary.service.account

import io.github.taetae98coding.diary.common.model.request.account.JoinRequest
import io.github.taetae98coding.diary.common.model.request.account.LoginRequest
import io.github.taetae98coding.diary.common.model.response.account.LoginResponse
import io.github.taetae98coding.diary.core.diary.service.DiaryServiceModule
import io.github.taetae98coding.diary.core.diary.service.ext.getOrThrow
import io.github.taetae98coding.diary.core.model.account.AccountToken
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
public class AccountService internal constructor(
	@Named(DiaryServiceModule.DIARY_CLIENT)
	private val client: HttpClient,
) {
	public suspend fun join(email: String, password: String) {
		client
			.post("/account/join") {
				val body =
					JoinRequest(
						email = email,
						password = password,
					)

				contentType(ContentType.Application.Json)
				setBody(body)
			}.getOrThrow<Unit>()
	}

	public suspend fun login(email: String, password: String): AccountToken {
		val response =
			client
				.post("/account/login") {
					val body =
						LoginRequest(
							email = email,
							password = password,
						)

					contentType(ContentType.Application.Json)
					setBody(body)
				}.getOrThrow<LoginResponse>()

		return AccountToken(
			uid = response.uid,
			token = response.token,
		)
	}
}
