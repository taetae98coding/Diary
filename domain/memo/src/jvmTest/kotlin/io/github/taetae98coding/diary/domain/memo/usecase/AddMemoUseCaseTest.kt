package io.github.taetae98coding.diary.domain.memo.usecase

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.core.model.memo.MemoTitleBlankException
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.memo.repository.AccountMemoRepository
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
import io.mockk.verify
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.flowOf

class AddMemoUseCaseTest : BehaviorSpec() {
    private val getAccountUseCase = mockk<GetAccountUseCase>()
    private val repository = mockk<AccountMemoRepository>()
    private val requestSyncUseCase = mockk<RequestSyncUseCase>(relaxed = true)
    private val useCase = AddMemoUseCase(getAccountUseCase, repository, requestSyncUseCase)

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        Given("User 계정이고 title이 유효한 MemoDetail") {
            clearAllMocks()
            val account = fixtureMonkey.giveMeOne<Account.User>()
            val detail = fixtureMonkey.giveMeKotlinBuilder<MemoDetail>()
                .set(MemoDetail::title, "title")
                .sample()

            val primaryTag = fixtureMonkey.giveMeOne<Uuid?>()
            val memoTagIds = fixtureMonkey.giveMeOne<Set<Uuid>>()

            every { getAccountUseCase() } returns flowOf(Result.success(account))
            coEvery { repository.add(account.accountId, detail, primaryTag, memoTagIds) } returns Unit

            When("AddMemoUseCase를 호출하면") {
                val result = useCase(detail, primaryTag, memoTagIds)

                Then("성공한다") {
                    result.shouldBeSuccess()
                }

                Then("GetAccountUseCase를 호출한 후 repository에 메모를 추가한다") {
                    coVerifyOrder {
                        getAccountUseCase()
                        repository.add(account.accountId, detail, primaryTag, memoTagIds)
                    }
                }

                Then("account의 accountId로 repository를 호출한다") {
                    coVerify(exactly = 1) { repository.add(account.accountId, detail, primaryTag, memoTagIds) }
                }

                Then("RequestSyncUseCase를 호출한다") {
                    coVerify(exactly = 1) { requestSyncUseCase() }
                }
            }
        }

        Given("Guest 계정이고 title이 유효한 MemoDetail") {
            clearAllMocks()
            val account = Account.Guest
            val detail = fixtureMonkey.giveMeKotlinBuilder<MemoDetail>()
                .set(MemoDetail::title, "title")
                .sample()

            val primaryTag = fixtureMonkey.giveMeOne<Uuid?>()
            val memoTagIds = fixtureMonkey.giveMeOne<Set<Uuid>>()

            every { getAccountUseCase() } returns flowOf(Result.success(account))
            coEvery { repository.add(account.accountId, detail, primaryTag, memoTagIds) } returns Unit

            When("AddMemoUseCase를 호출하면") {
                val result = useCase(detail, primaryTag, memoTagIds)

                Then("성공한다") {
                    result.shouldBeSuccess()
                }

                Then("RequestSyncUseCase를 호출한다") {
                    coVerify(exactly = 1) { requestSyncUseCase() }
                }
            }
        }

        Given("title이 공백 문자열인 MemoDetail") {
            clearAllMocks()
            val detail = fixtureMonkey.giveMeKotlinBuilder<MemoDetail>()
                .set(MemoDetail::title, " ")
                .sample()

            When("AddMemoUseCase를 호출하면") {
                val result = useCase(detail)

                Then("MemoTitleBlankException을 반환한다") {
                    result.shouldBeFailure<MemoTitleBlankException>()
                }

                Then("repository를 호출하지 않는다") {
                    coVerify(exactly = 0) { repository.add(any(), any(), any(), any()) }
                }

                Then("RequestSyncUseCase를 호출하지 않는다") {
                    coVerify(exactly = 0) { requestSyncUseCase() }
                }
            }
        }
    }
}
