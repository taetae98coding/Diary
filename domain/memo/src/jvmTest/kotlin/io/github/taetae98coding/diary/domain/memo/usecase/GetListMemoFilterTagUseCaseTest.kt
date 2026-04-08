package io.github.taetae98coding.diary.domain.memo.usecase

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.memo.repository.AccountListMemoFilterTagRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class GetListMemoFilterTagUseCaseTest : BehaviorSpec() {
    private val getAccountUseCase = mockk<GetAccountUseCase>()
    private val accountListMemoFilterTagRepository = mockk<AccountListMemoFilterTagRepository>()
    private val useCase = GetListMemoFilterTagUseCase(getAccountUseCase, accountListMemoFilterTagRepository)

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        Given("User 계정") {
            clearAllMocks()
            val account = fixtureMonkey.giveMeOne<Account.User>()
            val tags = fixtureMonkey.giveMeOne<List<Uuid>>()

            every { getAccountUseCase() } returns flowOf(Result.success(account))
            every { accountListMemoFilterTagRepository.get(account.accountId) } returns flowOf(tags)

            When("GetListMemoFilterTagUseCase를 호출하면") {
                val result = useCase().first()

                Then("성공한다") {
                    result.shouldBeSuccess(tags)
                }

                Then("GetAccountUseCase를 호출한다") {
                    verify(exactly = 1) { getAccountUseCase() }
                }

                Then("account의 accountId로 repository를 호출한다") {
                    verify(exactly = 1) { accountListMemoFilterTagRepository.get(account.accountId) }
                }
            }
        }

        Given("Guest 계정") {
            clearAllMocks()
            val account = Account.Guest
            val tags = fixtureMonkey.giveMeOne<List<Uuid>>()

            every { getAccountUseCase() } returns flowOf(Result.success(account))
            every { accountListMemoFilterTagRepository.get(account.accountId) } returns flowOf(tags)

            When("GetListMemoFilterTagUseCase를 호출하면") {
                val result = useCase().first()

                Then("성공한다") {
                    result.shouldBeSuccess(tags)
                }

                Then("Guest의 accountId로 repository를 호출한다") {
                    verify(exactly = 1) { accountListMemoFilterTagRepository.get(account.accountId) }
                }
            }
        }
    }
}
