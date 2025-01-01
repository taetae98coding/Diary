package io.github.taetae98coding.diary.feature.fcm

import io.github.taetae98coding.diary.common.model.request.fcm.DeleteFCMRequest
import io.github.taetae98coding.diary.common.model.request.fcm.UpsertFCMRequest
import io.github.taetae98coding.diary.common.model.response.DiaryResponse
import io.github.taetae98coding.diary.domain.fcm.usecase.DeleteFCMTokenUseCase
import io.github.taetae98coding.diary.domain.fcm.usecase.UpsertFCMTokenUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.plugin.scope

public fun Route.fcmRouting() {
	route("/fcm") {
		post<DeleteFCMRequest>("/delete") { request ->
			val useCase = call.scope.get<DeleteFCMTokenUseCase>()

			useCase(token = request.token)
				.onSuccess { call.respond(DiaryResponse.Success) }
				.onFailure { call.respond(HttpStatusCode.InternalServerError,DiaryResponse.InternalServerError) }
		}

		authenticate("account") {
			post<UpsertFCMRequest>("/upsert") { request ->
				val principal = call.principal<JWTPrincipal>()
				if (principal == null) {
					call.respond(HttpStatusCode.Unauthorized, DiaryResponse.Unauthorized)
					return@post
				}

				val useCase = call.scope.get<UpsertFCMTokenUseCase>()

				useCase(
					token = request.token,
					owner = principal.payload.getClaim("uid").asString(),
				).onSuccess {
					call.respond(DiaryResponse.Success)
				}.onFailure {
					call.respond(HttpStatusCode.InternalServerError,DiaryResponse.InternalServerError)
				}
			}
		}
	}
}
