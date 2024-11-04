package io.github.taetae98coding.diary.plugin

import io.ktor.http.HttpHeaders
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.cors.routing.CORS

internal fun Application.installCORS() {
	install(CORS) {
		anyHost()
		allowHeader(HttpHeaders.ContentType)
	}
}
