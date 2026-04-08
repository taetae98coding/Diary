package io.github.taetae98coding.diary.domain.sync.usecase

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.core.model.sync.SyncType
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.sync.manager.SyncManager
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf

class RequestSyncUseCaseTest : BehaviorSpec() {
    private val getAccountUseCase = mockk<GetAccountUseCase>()
    private val syncManager = mockk<SyncManager>(relaxed = true)
    private val useCase = RequestSyncUseCase(getAccountUseCase, syncManager)

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        Given("User 계정") {
            clearAllMocks()
            val account = fixtureMonkey.giveMeOne<Account.User>()
            val syncType = SyncType.Background

            every { getAccountUseCase() } returns flowOf(Result.success(account))

            When("RequestSyncUseCase를 호출하면") {
                useCase(syncType)

                Then("syncManager의 requestSync를 호출한다") {
                    verify(exactly = 1) { syncManager.requestSync(account.accountId, syncType) }
                }
            }
        }

        Given("User 계정이고 Foreground 동기화") {
            clearAllMocks()
            val account = fixtureMonkey.giveMeOne<Account.User>()
            val syncType = SyncType.Foreground

            every { getAccountUseCase() } returns flowOf(Result.success(account))

            When("RequestSyncUseCase를 호출하면") {
                useCase(syncType)

                Then("Foreground 타입으로 syncManager를 호출한다") {
                    verify(exactly = 1) { syncManager.requestSync(account.accountId, syncType) }
                }
            }
        }

        Given("Guest 계정") {
            clearAllMocks()
            val account = Account.Guest
            val syncType = SyncType.Background

            every { getAccountUseCase() } returns flowOf(Result.success(account))

            When("RequestSyncUseCase를 호출하면") {
                useCase(syncType)

                Then("syncManager를 호출하지 않는다") {
                    verify(exactly = 0) { syncManager.requestSync(any(), any()) }
                }
            }
        }
    }
}
