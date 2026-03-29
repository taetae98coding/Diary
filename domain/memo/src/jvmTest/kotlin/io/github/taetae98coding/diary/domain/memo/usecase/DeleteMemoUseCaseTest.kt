package io.github.taetae98coding.diary.domain.memo.usecase

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.memo.repository.AccountMemoRepository
import io.github.taetae98coding.diary.domain.sync.usecase.RequestSyncUseCase
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

class DeleteMemoUseCaseTest : BehaviorSpec() {
    private val getAccountUseCase = mockk<GetAccountUseCase>()
    private val accountMemoRepository = mockk<AccountMemoRepository>()
    private val requestSyncUseCase = mockk<RequestSyncUseCase>(relaxed = true)
    private val useCase = DeleteMemoUseCase(getAccountUseCase, accountMemoRepository, requestSyncUseCase)

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        Given("User 계정이고 유효한 memoId") {
            clearAllMocks()
            val memoId = Uuid.random()
            val account = fixtureMonkey.giveMeOne<Account.User>()

            every { getAccountUseCase() } returns flowOf(Result.success(account))
            coEvery { accountMemoRepository.updateDelete(memoId = memoId, isDeleted = true) } returns Unit

            When("DeleteMemoUseCase를 호출하면") {
                val result = useCase(memoId)

                Then("성공한다") {
                    result.shouldBeSuccess()
                }

                Then("repository의 updateDelete를 호출한다") {
                    coVerify(exactly = 1) { accountMemoRepository.updateDelete(memoId = memoId, isDeleted = true) }
                }

                Then("RequestSyncUseCase를 호출한다") {
                    coVerify(exactly = 1) { requestSyncUseCase() }
                }
            }
        }

        Given("Guest 계정이고 유효한 memoId") {
            clearAllMocks()
            val memoId = Uuid.random()
            val account = Account.Guest

            every { getAccountUseCase() } returns flowOf(Result.success(account))
            coEvery { accountMemoRepository.updateDelete(memoId = memoId, isDeleted = true) } returns Unit

            When("DeleteMemoUseCase를 호출하면") {
                val result = useCase(memoId)

                Then("성공한다") {
                    result.shouldBeSuccess()
                }

                Then("RequestSyncUseCase를 호출한다") {
                    coVerify(exactly = 1) { requestSyncUseCase() }
                }
            }
        }
    }
}
