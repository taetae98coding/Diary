package io.github.taetae98coding.diary.feature.account

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.github.taetae98coding.diary.common.exception.account.ExistEmailException
import io.github.taetae98coding.diary.common.model.request.account.JoinRequest
import io.github.taetae98coding.diary.common.model.request.account.LoginRequest
import io.github.taetae98coding.diary.common.model.response.DiaryResponse
import io.github.taetae98coding.diary.common.model.response.account.LoginResponse
import io.github.taetae98coding.diary.domain.account.usecase.FindAccountUseCase
import io.github.taetae98coding.diary.domain.account.usecase.JoinUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.coroutines.flow.first
import org.koin.ktor.plugin.scope

public fun Route.accountRouting() {
	route("/account") {
		post<JoinRequest>("/join") { request ->
			val useCase = call.scope.get<JoinUseCase>()

			useCase(request.email, request.password)
				.onSuccess { call.respond(HttpStatusCode.Created, DiaryResponse.Created) }
				.onFailure {
					when (it) {
						is ExistEmailException -> call.respond(HttpStatusCode.Forbidden, DiaryResponse.AlreadyExistEmail)
						else -> call.respond(HttpStatusCode.InternalServerError, DiaryResponse.InternalServerError)
					}
				}
		}

		post<LoginRequest>("/login") { request ->
			val useCase = call.scope.get<FindAccountUseCase>()

			useCase(request.email, request.password).first()
				.onSuccess { account ->
					if (account == null) {
						call.respond(HttpStatusCode.NotFound, DiaryResponse.AccountNotFound)
						return@onSuccess
					}

					val token =
						JWT
							.create()
							.withClaim("uid", account.uid)
							.sign(Algorithm.HMAC256("secret"))

					val response =
						LoginResponse(
							uid = account.uid,
							token = token,
						)

					call.respond(DiaryResponse.success(response))
				}.onFailure {
					call.respond(HttpStatusCode.InternalServerError, DiaryResponse.InternalServerError)
				}
		}
	}
}
