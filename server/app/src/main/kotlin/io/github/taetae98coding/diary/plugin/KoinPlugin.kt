package io.github.taetae98coding.diary.plugin

import io.github.taetae98coding.diary.data.account.AccountDataModule
import io.github.taetae98coding.diary.data.memo.MemoDataModule
import io.github.taetae98coding.diary.domain.account.AccountDomainModule
import io.github.taetae98coding.diary.domain.memo.MemoDomainModule
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ksp.generated.module
import org.koin.ktor.plugin.Koin

internal fun Application.installKoin() {
	install(Koin) {
		modules(
			AccountDataModule().module,
			MemoDataModule().module,
			AccountDomainModule().module,
			MemoDomainModule().module,
		)
	}
}
