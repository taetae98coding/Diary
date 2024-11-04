package io.github.taetae98coding.diary.plugin

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.github.taetae98coding.diary.common.model.response.DiaryResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.response.respond

internal fun Application.installAuth() {
	install(Authentication) {
		jwt("account") {
			val verifier =
				JWT
					.require(Algorithm.HMAC256("secret"))
					.build()

			verifier(verifier)

			validate { credential ->
				if (credential.payload.getClaim("uid").asString() != "") {
					JWTPrincipal(credential.payload)
				} else {
					null
				}
			}

			challenge { _, _ ->
				call.respond(HttpStatusCode.Unauthorized, DiaryResponse.Unauthorized)
			}
		}
	}
}
