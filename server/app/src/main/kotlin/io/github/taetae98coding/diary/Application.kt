package io.github.taetae98coding.diary

import io.github.taetae98coding.diary.plugin.installAuth
import io.github.taetae98coding.diary.plugin.installCORS
import io.github.taetae98coding.diary.plugin.installContentNegotiation
import io.github.taetae98coding.diary.plugin.installDatabase
import io.github.taetae98coding.diary.plugin.installKoin
import io.github.taetae98coding.diary.plugin.installRouting
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain

public fun main(args: Array<String>) {
	EngineMain.main(args)
}

public fun Application.module() {
	installCORS()
	installDatabase()
	installKoin()
	installAuth()
	installContentNegotiation()
	installRouting()
}
