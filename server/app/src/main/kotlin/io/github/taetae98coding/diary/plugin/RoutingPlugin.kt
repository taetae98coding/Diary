package io.github.taetae98coding.diary.plugin

import io.github.taetae98coding.diary.feature.account.accountRouting
import io.github.taetae98coding.diary.feature.home.homeRouting
import io.github.taetae98coding.diary.feature.memo.memoRouting
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

internal fun Application.installRouting() {
	routing {
		homeRouting()
		accountRouting()
		memoRouting()
	}
}
