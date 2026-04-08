package io.github.taetae98coding.diary.domain.tag.usecase

import androidx.paging.PagingData
import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.tag.repository.AccountTagRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class PageTagUseCaseTest : BehaviorSpec() {
    private val getAccountUseCase = mockk<GetAccountUseCase>()
    private val accountTagRepository = mockk<AccountTagRepository>()
    private val useCase = PageTagUseCase(getAccountUseCase, accountTagRepository)

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        Given("유효한 계정") {
            clearAllMocks()
            val account = fixtureMonkey.giveMeOne<Account.User>()
            val pagingData = PagingData.empty<io.github.taetae98coding.diary.core.model.tag.Tag>()

            every { getAccountUseCase() } returns flowOf(Result.success(account))
            every { accountTagRepository.page(account.accountId) } returns flowOf(pagingData)

            When("PageTagUseCase를 호출하면") {
                val result = useCase().first()

                Then("성공한다") {
                    result.shouldBeSuccess()
                }

                Then("accountTagRepository의 page를 호출한다") {
                    verify(exactly = 1) { accountTagRepository.page(account.accountId) }
                }
            }
        }
    }
}
