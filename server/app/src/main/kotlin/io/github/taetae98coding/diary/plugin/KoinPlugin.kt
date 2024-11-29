package io.github.taetae98coding.diary.plugin

import io.github.taetae98coding.diary.data.account.AccountDataModule
import io.github.taetae98coding.diary.data.buddy.BuddyDataModule
import io.github.taetae98coding.diary.data.fcm.FCMDataModule
import io.github.taetae98coding.diary.data.memo.MemoDataModule
import io.github.taetae98coding.diary.data.tag.TagDataModule
import io.github.taetae98coding.diary.domain.account.AccountDomainModule
import io.github.taetae98coding.diary.domain.buddy.BuddyDomainModule
import io.github.taetae98coding.diary.domain.fcm.FCMDomainModule
import io.github.taetae98coding.diary.domain.memo.MemoDomainModule
import io.github.taetae98coding.diary.domain.tag.TagDomainModule
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ksp.generated.module
import org.koin.ktor.plugin.Koin

internal fun Application.installKoin() {
	install(Koin) {
		modules(
			AccountDataModule().module,
			MemoDataModule().module,
			TagDataModule().module,
			FCMDataModule().module,
			BuddyDataModule().module,
			AccountDomainModule().module,
			MemoDomainModule().module,
			TagDomainModule().module,
			FCMDomainModule().module,
			BuddyDomainModule().module,
		)
	}
}
