package io.github.taetae98coding.diary

import io.github.taetae98coding.diary.feature.home.homeRouting
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain
import io.ktor.server.routing.routing

public fun main(args: Array<String>) {
	EngineMain.main(args)
}

public fun Application.module() {
	routing {
		homeRouting()
	}
}
