package io.github.taetae98coding.diary.domain.memo.usecase

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.memo.repository.AccountCalendarMemoFilterTagRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class HasCalendarMemoFilterUseCaseTest : BehaviorSpec() {
    private val getAccountUseCase = mockk<GetAccountUseCase>()
    private val accountCalendarMemoFilterTagRepository = mockk<AccountCalendarMemoFilterTagRepository>()
    private val useCase = HasCalendarMemoFilterUseCase(getAccountUseCase, accountCalendarMemoFilterTagRepository)

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        Given("필터 태그가 존재하는 경우") {
            clearAllMocks()
            val account = fixtureMonkey.giveMeOne<Account.User>()
            val tags = listOf(Uuid.random())

            every { getAccountUseCase() } returns flowOf(Result.success(account))
            every { accountCalendarMemoFilterTagRepository.get(account.accountId) } returns flowOf(tags)

            When("HasCalendarMemoFilterUseCase를 호출하면") {
                val result = useCase().first()

                Then("true를 반환한다") {
                    result.shouldBeSuccess(true)
                }

                Then("account의 accountId로 repository를 호출한다") {
                    verify(exactly = 1) { accountCalendarMemoFilterTagRepository.get(account.accountId) }
                }
            }
        }

        Given("필터 태그가 비어있는 경우") {
            clearAllMocks()
            val account = fixtureMonkey.giveMeOne<Account.User>()
            val tags = emptyList<Uuid>()

            every { getAccountUseCase() } returns flowOf(Result.success(account))
            every { accountCalendarMemoFilterTagRepository.get(account.accountId) } returns flowOf(tags)

            When("HasCalendarMemoFilterUseCase를 호출하면") {
                val result = useCase().first()

                Then("false를 반환한다") {
                    result.shouldBeSuccess(false)
                }
            }
        }
    }
}
