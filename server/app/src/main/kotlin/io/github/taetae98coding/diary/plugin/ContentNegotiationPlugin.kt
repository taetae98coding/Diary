package io.github.taetae98coding.diary.plugin

import io.ktor.serialization.kotlinx.json.DefaultJson
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json

internal fun Application.installContentNegotiation() {
	install(ContentNegotiation) {
		json(
			json =
			Json(DefaultJson) {
				ignoreUnknownKeys = true
			},
		)
	}
}
