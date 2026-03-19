package io.github.taetae98coding.diary.domain.sync.usecase

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.sync.repository.SyncRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

class SyncUseCaseTest : BehaviorSpec() {
    private val getAccountUseCase = mockk<GetAccountUseCase>()
    private val syncRepository = mockk<SyncRepository>()
    private val useCase = SyncUseCase(getAccountUseCase, syncRepository)

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        Given("User 계정") {
            clearAllMocks()
            val account = fixtureMonkey.giveMeOne<Account.User>()

            every { getAccountUseCase() } returns flowOf(Result.success(account))
            coEvery { syncRepository.sync(account.accountId) } returns Unit

            When("SyncUseCase를 호출하면") {
                val result = useCase()

                Then("성공한다") {
                    result.shouldBeSuccess()
                }

                Then("SyncRepository.sync를 호출한다") {
                    coVerify(exactly = 1) { syncRepository.sync(account.accountId) }
                }
            }
        }

        Given("Guest 계정") {
            clearAllMocks()
            val account = Account.Guest

            every { getAccountUseCase() } returns flowOf(Result.success(account))

            When("SyncUseCase를 호출하면") {
                val result = useCase()

                Then("성공한다") {
                    result.shouldBeSuccess()
                }

                Then("SyncRepository.sync를 호출하지 않는다") {
                    coVerify(exactly = 0) { syncRepository.sync(any()) }
                }
            }
        }
    }
}
