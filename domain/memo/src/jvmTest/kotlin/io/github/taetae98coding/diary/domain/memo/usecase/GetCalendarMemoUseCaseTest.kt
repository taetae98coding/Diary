package io.github.taetae98coding.diary.domain.memo.usecase

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.core.model.memo.CalendarMemo
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.memo.repository.AccountCalendarMemoRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDateTime

class GetCalendarMemoUseCaseTest : BehaviorSpec() {
    private val getAccountUseCase = mockk<GetAccountUseCase>()
    private val accountCalendarMemoRepository = mockk<AccountCalendarMemoRepository>()
    private val useCase = GetCalendarMemoUseCase(getAccountUseCase, accountCalendarMemoRepository)

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        Given("User 계정") {
            clearAllMocks()
            val account = fixtureMonkey.giveMeOne<Account.User>()
            val year = 2026
            val memos = listOf(
                fixtureMonkey.giveMeKotlinBuilder<CalendarMemo>()
                    .set(CalendarMemo::localDateTimeRange, LocalDateTime(2026, 1, 1, 0, 0)..LocalDateTime(2026, 1, 31, 23, 59))
                    .sample(),
            )

            every { getAccountUseCase() } returns flowOf(Result.success(account))
            every { accountCalendarMemoRepository.get(account.accountId, year) } returns flowOf(memos)

            When("GetCalendarMemoUseCase를 호출하면") {
                val result = useCase(year).first()

                Then("성공한다") {
                    result.shouldBeSuccess(memos)
                }

                Then("GetAccountUseCase를 호출한다") {
                    verify(exactly = 1) { getAccountUseCase() }
                }

                Then("account의 accountId와 year로 repository를 호출한다") {
                    verify(exactly = 1) { accountCalendarMemoRepository.get(account.accountId, year) }
                }
            }
        }

        Given("Guest 계정") {
            clearAllMocks()
            val account = Account.Guest
            val year = 2026
            val memos = listOf(
                fixtureMonkey.giveMeKotlinBuilder<CalendarMemo>()
                    .set(CalendarMemo::localDateTimeRange, LocalDateTime(2026, 1, 1, 0, 0)..LocalDateTime(2026, 1, 31, 23, 59))
                    .sample(),
            )

            every { getAccountUseCase() } returns flowOf(Result.success(account))
            every { accountCalendarMemoRepository.get(account.accountId, year) } returns flowOf(memos)

            When("GetCalendarMemoUseCase를 호출하면") {
                val result = useCase(year).first()

                Then("성공한다") {
                    result.shouldBeSuccess(memos)
                }

                Then("Guest의 accountId로 repository를 호출한다") {
                    verify(exactly = 1) { accountCalendarMemoRepository.get(account.accountId, year) }
                }
            }
        }
    }
}
