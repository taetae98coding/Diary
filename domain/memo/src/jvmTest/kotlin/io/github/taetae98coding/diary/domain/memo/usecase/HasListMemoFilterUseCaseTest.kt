package io.github.taetae98coding.diary.domain.memo.usecase

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import io.github.taetae98coding.diary.core.model.FilterPresence
import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.core.model.memo.ListMemoFilterOption
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.memo.repository.AccountListMemoFilterTagRepository
import io.github.taetae98coding.diary.domain.memo.repository.ListMemoFilterOptionRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class HasListMemoFilterUseCaseTest : BehaviorSpec() {
    private val getAccountUseCase = mockk<GetAccountUseCase>()
    private val accountListMemoFilterTagRepository = mockk<AccountListMemoFilterTagRepository>()
    private val listMemoFilterOptionRepository = mockk<ListMemoFilterOptionRepository>()
    private val useCase = HasListMemoFilterUseCase(accountListMemoFilterTagRepository, listMemoFilterOptionRepository, getAccountUseCase)

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        Given("필터 태그가 존재하는 경우") {
            clearAllMocks()
            val account = fixtureMonkey.giveMeOne<Account.User>()
            val tags = setOf(Uuid.random())
            val option = ListMemoFilterOption()

            every { getAccountUseCase() } returns flowOf(Result.success(account))
            every { accountListMemoFilterTagRepository.get(account.accountId) } returns flowOf(tags)
            every { listMemoFilterOptionRepository.get() } returns flowOf(option)

            When("HasListMemoFilterUseCase를 호출하면") {
                val result = useCase().first()

                Then("true를 반환한다") {
                    result.shouldBeSuccess(true)
                }
            }
        }

        Given("태그는 비어있지만 tagPresence가 NONE이 아닌 경우") {
            clearAllMocks()
            val account = fixtureMonkey.giveMeOne<Account.User>()
            val tags = emptySet<Uuid>()
            val option = ListMemoFilterOption(tagPresence = FilterPresence.YES)

            every { getAccountUseCase() } returns flowOf(Result.success(account))
            every { accountListMemoFilterTagRepository.get(account.accountId) } returns flowOf(tags)
            every { listMemoFilterOptionRepository.get() } returns flowOf(option)

            When("HasListMemoFilterUseCase를 호출하면") {
                val result = useCase().first()

                Then("true를 반환한다") {
                    result.shouldBeSuccess(true)
                }
            }
        }

        Given("태그는 비어있지만 datePresence가 NONE이 아닌 경우") {
            clearAllMocks()
            val account = fixtureMonkey.giveMeOne<Account.User>()
            val tags = emptySet<Uuid>()
            val option = ListMemoFilterOption(datePresence = FilterPresence.NO)

            every { getAccountUseCase() } returns flowOf(Result.success(account))
            every { accountListMemoFilterTagRepository.get(account.accountId) } returns flowOf(tags)
            every { listMemoFilterOptionRepository.get() } returns flowOf(option)

            When("HasListMemoFilterUseCase를 호출하면") {
                val result = useCase().first()

                Then("true를 반환한다") {
                    result.shouldBeSuccess(true)
                }
            }
        }

        Given("태그가 비어있고 모든 필터 옵션이 NONE인 경우") {
            clearAllMocks()
            val account = fixtureMonkey.giveMeOne<Account.User>()
            val tags = emptySet<Uuid>()
            val option = ListMemoFilterOption()

            every { getAccountUseCase() } returns flowOf(Result.success(account))
            every { accountListMemoFilterTagRepository.get(account.accountId) } returns flowOf(tags)
            every { listMemoFilterOptionRepository.get() } returns flowOf(option)

            When("HasListMemoFilterUseCase를 호출하면") {
                val result = useCase().first()

                Then("false를 반환한다") {
                    result.shouldBeSuccess(false)
                }
            }
        }
    }
}
