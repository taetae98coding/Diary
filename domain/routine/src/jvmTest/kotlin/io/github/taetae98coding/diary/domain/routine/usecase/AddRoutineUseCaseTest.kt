package io.github.taetae98coding.diary.domain.routine.usecase

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.core.model.routine.RoutineDetail
import io.github.taetae98coding.diary.core.model.routine.RoutineRRule
import io.github.taetae98coding.diary.core.model.routine.RoutineRRulesEmptyException
import io.github.taetae98coding.diary.core.model.routine.RoutineTitleBlankException
import io.github.taetae98coding.diary.core.model.sync.SyncType
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.routine.repository.AccountRoutineRepository
import io.github.taetae98coding.diary.domain.sync.usecase.RequestSyncUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.DayOfWeek

class AddRoutineUseCaseTest : BehaviorSpec() {
    private val getAccountUseCase = mockk<GetAccountUseCase>()
    private val requestSyncUseCase = mockk<RequestSyncUseCase>(relaxed = true)
    private val repository = mockk<AccountRoutineRepository>()
    private val useCase = AddRoutineUseCase(getAccountUseCase, requestSyncUseCase, repository)

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        Given("User 계정이고 유효한 RoutineDetail과 rrules") {
            clearAllMocks()
            val account = fixtureMonkey.giveMeOne<Account.User>()
            val detail = fixtureMonkey.giveMeKotlinBuilder<RoutineDetail>()
                .set(RoutineDetail::title, "title")
                .sample()
            val rRules = listOf(RoutineRRule.ByDay(dayOfWeek = DayOfWeek.MONDAY))

            every { getAccountUseCase() } returns flowOf(Result.success(account))
            coEvery { repository.add(account.accountId, detail, rRules) } returns Unit

            When("AddRoutineUseCase를 호출하면") {
                val result = useCase(detail, rRules)

                Then("성공한다") {
                    result.shouldBeSuccess()
                }

                Then("GetAccountUseCase를 호출한 후 repository에 루틴을 추가한다") {
                    coVerifyOrder {
                        getAccountUseCase()
                        repository.add(account.accountId, detail, rRules)
                    }
                }

                Then("account의 accountId로 repository를 호출한다") {
                    coVerify(exactly = 1) { repository.add(account.accountId, detail, rRules) }
                }

                Then("RequestSyncUseCase를 호출한다") {
                    coVerify(exactly = 1) { requestSyncUseCase(SyncType.Background) }
                }
            }
        }

        Given("Guest 계정이고 유효한 RoutineDetail과 rrules") {
            clearAllMocks()
            val account = Account.Guest
            val detail = fixtureMonkey.giveMeKotlinBuilder<RoutineDetail>()
                .set(RoutineDetail::title, "title")
                .sample()
            val rRules = listOf(RoutineRRule.ByMonthDay(day = 15))

            every { getAccountUseCase() } returns flowOf(Result.success(account))
            coEvery { repository.add(account.accountId, detail, rRules) } returns Unit

            When("AddRoutineUseCase를 호출하면") {
                val result = useCase(detail, rRules)

                Then("성공한다") {
                    result.shouldBeSuccess()
                }

                Then("RequestSyncUseCase를 호출한다") {
                    coVerify(exactly = 1) { requestSyncUseCase(SyncType.Background) }
                }
            }
        }

        Given("title이 공백 문자열인 RoutineDetail") {
            clearAllMocks()
            val detail = fixtureMonkey.giveMeKotlinBuilder<RoutineDetail>()
                .set(RoutineDetail::title, " ")
                .sample()
            val rRules = listOf(RoutineRRule.ByDay(dayOfWeek = DayOfWeek.MONDAY))

            When("AddRoutineUseCase를 호출하면") {
                val result = useCase(detail, rRules)

                Then("RoutineTitleBlankException을 반환한다") {
                    result.shouldBeFailure<RoutineTitleBlankException>()
                }

                Then("repository를 호출하지 않는다") {
                    coVerify(exactly = 0) { repository.add(any(), any(), any()) }
                }

                Then("RequestSyncUseCase를 호출하지 않는다") {
                    coVerify(exactly = 0) { requestSyncUseCase(SyncType.Background) }
                }
            }
        }

        Given("rrules가 빈 리스트") {
            clearAllMocks()
            val detail = fixtureMonkey.giveMeKotlinBuilder<RoutineDetail>()
                .set(RoutineDetail::title, "title")
                .sample()
            val rRules = emptyList<RoutineRRule>()

            When("AddRoutineUseCase를 호출하면") {
                val result = useCase(detail, rRules)

                Then("RoutineRRulesEmptyException을 반환한다") {
                    result.shouldBeFailure<RoutineRRulesEmptyException>()
                }

                Then("repository를 호출하지 않는다") {
                    coVerify(exactly = 0) { repository.add(any(), any(), any()) }
                }

                Then("RequestSyncUseCase를 호출하지 않는다") {
                    coVerify(exactly = 0) { requestSyncUseCase(SyncType.Background) }
                }
            }
        }
    }
}
