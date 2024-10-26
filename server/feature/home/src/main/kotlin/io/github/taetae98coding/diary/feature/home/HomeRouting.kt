package io.github.taetae98coding.diary.feature.home

import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

public fun Route.homeRouting() {
	get {
		call.respondText(text = "Diary")
	}
}
