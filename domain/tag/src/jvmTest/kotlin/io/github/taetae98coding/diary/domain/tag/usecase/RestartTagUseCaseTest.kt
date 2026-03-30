package io.github.taetae98coding.diary.domain.tag.usecase

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.core.model.sync.SyncType
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.sync.usecase.RequestSyncUseCase
import io.github.taetae98coding.diary.domain.tag.repository.AccountTagRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.flowOf

class RestartTagUseCaseTest : BehaviorSpec() {
    private val getAccountUseCase = mockk<GetAccountUseCase>()
    private val accountTagRepository = mockk<AccountTagRepository>()
    private val requestSyncUseCase = mockk<RequestSyncUseCase>(relaxed = true)
    private val useCase = RestartTagUseCase(getAccountUseCase, accountTagRepository, requestSyncUseCase)

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        Given("User 계정이고 유효한 tagId") {
            clearAllMocks()
            val tagId = Uuid.random()
            val account = fixtureMonkey.giveMeOne<Account.User>()

            every { getAccountUseCase() } returns flowOf(Result.success(account))
            coEvery { accountTagRepository.updateFinish(tagId = tagId, isFinished = false) } returns Unit

            When("RestartTagUseCase를 호출하면") {
                val result = useCase(tagId)

                Then("성공한다") {
                    result.shouldBeSuccess()
                }

                Then("repository의 updateFinish를 isFinished=false로 호출한다") {
                    coVerify(exactly = 1) { accountTagRepository.updateFinish(tagId = tagId, isFinished = false) }
                }

                Then("RequestSyncUseCase를 호출한다") {
                    coVerify(exactly = 1) { requestSyncUseCase(SyncType.Background) }
                }
            }
        }

        Given("Guest 계정이고 유효한 tagId") {
            clearAllMocks()
            val tagId = Uuid.random()
            val account = Account.Guest

            every { getAccountUseCase() } returns flowOf(Result.success(account))
            coEvery { accountTagRepository.updateFinish(tagId = tagId, isFinished = false) } returns Unit

            When("RestartTagUseCase를 호출하면") {
                val result = useCase(tagId)

                Then("성공한다") {
                    result.shouldBeSuccess()
                }

                Then("RequestSyncUseCase를 호출한다") {
                    coVerify(exactly = 1) { requestSyncUseCase(SyncType.Background) }
                }
            }
        }
    }
}
