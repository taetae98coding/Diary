package io.github.taetae98coding.diary.domain.memo.usecase

import androidx.paging.PagingData
import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.memo.repository.AccountMemoRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class PageMemoByTagUseCaseTest : BehaviorSpec() {
    private val getAccountUseCase = mockk<GetAccountUseCase>()
    private val accountMemoRepository = mockk<AccountMemoRepository>()
    private val useCase = PageMemoByTagUseCase(getAccountUseCase, accountMemoRepository)

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        Given("유효한 계정과 tagId") {
            clearAllMocks()
            val account = fixtureMonkey.giveMeOne<Account.User>()
            val tagId = Uuid.random()
            val pagingData = PagingData.empty<Memo>()

            every { getAccountUseCase() } returns flowOf(Result.success(account))
            every { accountMemoRepository.pageByTag(account.accountId, tagId) } returns flowOf(pagingData)

            When("PageMemoByTagUseCase를 호출하면") {
                val result = useCase(tagId).first()

                Then("성공한다") {
                    result.shouldBeSuccess()
                }

                Then("GetAccountUseCase를 호출한다") {
                    verify(exactly = 1) { getAccountUseCase() }
                }

                Then("account의 accountId와 tagId로 repository를 호출한다") {
                    verify(exactly = 1) { accountMemoRepository.pageByTag(account.accountId, tagId) }
                }
            }
        }

        Given("Guest 계정과 tagId") {
            clearAllMocks()
            val account = Account.Guest
            val tagId = Uuid.random()
            val pagingData = PagingData.empty<Memo>()

            every { getAccountUseCase() } returns flowOf(Result.success(account))
            every { accountMemoRepository.pageByTag(account.accountId, tagId) } returns flowOf(pagingData)

            When("PageMemoByTagUseCase를 호출하면") {
                val result = useCase(tagId).first()

                Then("성공한다") {
                    result.shouldBeSuccess()
                }

                Then("Guest의 accountId로 repository를 호출한다") {
                    verify(exactly = 1) { accountMemoRepository.pageByTag(account.accountId, tagId) }
                }
            }
        }
    }
}
