package io.github.taetae98coding.diary.domain.memo.usecase

import androidx.paging.PagingData
import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import io.github.taetae98coding.diary.core.model.FilterPresence
import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.core.model.memo.ListMemoFilterOption
import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.memo.repository.AccountListMemoRepository
import io.github.taetae98coding.diary.domain.memo.repository.ListMemoFilterOptionRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class PageListMemoUseCaseTest : BehaviorSpec() {
    private val getAccountUseCase = mockk<GetAccountUseCase>()
    private val accountListMemoRepository = mockk<AccountListMemoRepository>()
    private val listMemoFilterOptionRepository = mockk<ListMemoFilterOptionRepository>()
    private val useCase = PageListMemoUseCase(getAccountUseCase, accountListMemoRepository, listMemoFilterOptionRepository)

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        Given("유효한 계정과 필터 옵션") {
            clearAllMocks()
            val account = fixtureMonkey.giveMeOne<Account.User>()
            val option = ListMemoFilterOption(tagPresence = FilterPresence.YES, datePresence = FilterPresence.NO)
            val pagingData = PagingData.empty<Memo>()

            every { getAccountUseCase() } returns flowOf(Result.success(account))
            every { listMemoFilterOptionRepository.get() } returns flowOf(option)
            every {
                accountListMemoRepository.page(
                    accountId = account.accountId,
                    tagPresence = option.tagPresence,
                    datePresence = option.datePresence,
                )
            } returns flowOf(pagingData)

            When("PageListMemoUseCase를 호출하면") {
                val result = useCase().first()

                Then("성공한다") {
                    result.shouldBeSuccess()
                }

                Then("필터 옵션과 함께 repository의 page를 호출한다") {
                    verify(exactly = 1) {
                        accountListMemoRepository.page(
                            accountId = account.accountId,
                            tagPresence = option.tagPresence,
                            datePresence = option.datePresence,
                        )
                    }
                }
            }
        }

        Given("기본 필터 옵션") {
            clearAllMocks()
            val account = fixtureMonkey.giveMeOne<Account.User>()
            val option = ListMemoFilterOption()
            val pagingData = PagingData.empty<Memo>()

            every { getAccountUseCase() } returns flowOf(Result.success(account))
            every { listMemoFilterOptionRepository.get() } returns flowOf(option)
            every {
                accountListMemoRepository.page(
                    accountId = account.accountId,
                    tagPresence = FilterPresence.NONE,
                    datePresence = FilterPresence.NONE,
                )
            } returns flowOf(pagingData)

            When("PageListMemoUseCase를 호출하면") {
                val result = useCase().first()

                Then("성공한다") {
                    result.shouldBeSuccess()
                }

                Then("NONE 필터로 repository의 page를 호출한다") {
                    verify(exactly = 1) {
                        accountListMemoRepository.page(
                            accountId = account.accountId,
                            tagPresence = FilterPresence.NONE,
                            datePresence = FilterPresence.NONE,
                        )
                    }
                }
            }
        }
    }
}
