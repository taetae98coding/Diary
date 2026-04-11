package io.github.taetae98coding.diary.domain.tag.usecase

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.core.model.sync.SyncType
import io.github.taetae98coding.diary.core.model.tag.TagDetail
import io.github.taetae98coding.diary.core.model.tag.TagTitleBlankException
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.sync.usecase.RequestSyncUseCase
import io.github.taetae98coding.diary.domain.tag.repository.AccountTagRepository
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

class AddTagUseCaseTest : BehaviorSpec() {
    private val getAccountUseCase = mockk<GetAccountUseCase>()
    private val repository = mockk<AccountTagRepository>()
    private val requestSyncUseCase = mockk<RequestSyncUseCase>(relaxed = true)
    private val useCase = AddTagUseCase(getAccountUseCase, requestSyncUseCase, repository)

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        Given("User 계정이고 title이 유효한 TagDetail") {
            clearAllMocks()
            val account = fixtureMonkey.giveMeOne<Account.User>()
            val detail = fixtureMonkey.giveMeKotlinBuilder<TagDetail>()
                .set(TagDetail::title, "title")
                .sample()

            every { getAccountUseCase() } returns flowOf(Result.success(account))
            coEvery { repository.add(account.accountId, detail) } returns Unit

            When("AddTagUseCase를 호출하면") {
                val result = useCase(detail)

                Then("성공한다") {
                    result.shouldBeSuccess()
                }

                Then("GetAccountUseCase를 호출한 후 repository에 태그를 추가한다") {
                    coVerifyOrder {
                        getAccountUseCase()
                        repository.add(account.accountId, detail)
                    }
                }

                Then("account의 accountId로 repository를 호출한다") {
                    coVerify(exactly = 1) { repository.add(account.accountId, detail) }
                }

                Then("RequestSyncUseCase를 호출한다") {
                    coVerify(exactly = 1) { requestSyncUseCase(SyncType.Background) }
                }
            }
        }

        Given("Guest 계정이고 title이 유효한 TagDetail") {
            clearAllMocks()
            val account = Account.Guest
            val detail = fixtureMonkey.giveMeKotlinBuilder<TagDetail>()
                .set(TagDetail::title, "title")
                .sample()

            every { getAccountUseCase() } returns flowOf(Result.success(account))
            coEvery { repository.add(account.accountId, detail) } returns Unit

            When("AddTagUseCase를 호출하면") {
                val result = useCase(detail)

                Then("성공한다") {
                    result.shouldBeSuccess()
                }

                Then("RequestSyncUseCase를 호출한다") {
                    coVerify(exactly = 1) { requestSyncUseCase(SyncType.Background) }
                }
            }
        }

        Given("title이 공백 문자열인 TagDetail") {
            clearAllMocks()
            val detail = fixtureMonkey.giveMeKotlinBuilder<TagDetail>()
                .set(TagDetail::title, " ")
                .sample()

            When("AddTagUseCase를 호출하면") {
                val result = useCase(detail)

                Then("TagTitleBlankException을 반환한다") {
                    result.shouldBeFailure<TagTitleBlankException>()
                }

                Then("repository를 호출하지 않는다") {
                    coVerify(exactly = 0) { repository.add(any(), any()) }
                }

                Then("RequestSyncUseCase를 호출하지 않는다") {
                    coVerify(exactly = 0) { requestSyncUseCase(SyncType.Background) }
                }
            }
        }
    }
}
