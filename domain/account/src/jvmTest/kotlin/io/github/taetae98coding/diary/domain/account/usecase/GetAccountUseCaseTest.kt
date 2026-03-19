package io.github.taetae98coding.diary.domain.account.usecase

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.core.model.account.AccountInfo
import io.github.taetae98coding.diary.core.model.account.AccountMetaData
import io.github.taetae98coding.diary.domain.account.repository.AccountInfoRepository
import io.github.taetae98coding.diary.domain.account.repository.AccountMetaDataRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class GetAccountUseCaseTest : BehaviorSpec() {
    private val accountInfoRepository = mockk<AccountInfoRepository>()
    private val accountMetaDataRepository = mockk<AccountMetaDataRepository>()
    private val useCase = GetAccountUseCase(accountInfoRepository, accountMetaDataRepository)

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        Given("AccountInfo가 null") {
            clearAllMocks()

            every { accountInfoRepository.get() } returns flowOf(null)
            every { accountMetaDataRepository.get() } returns flowOf(null)

            When("GetAccountUseCase를 호출하면") {
                val result = useCase().first()

                Then("Account.Guest를 반환한다") {
                    result.shouldBeSuccess(Account.Guest)
                }

                Then("AccountInfoRepository를 호출한다") {
                    verify(exactly = 1) { accountInfoRepository.get() }
                }
            }
        }

        Given("AccountInfo가 존재하고 AccountMetaData가 존재") {
            clearAllMocks()

            val accountInfo = fixtureMonkey.giveMeOne<AccountInfo>()
            val accountMetaData = fixtureMonkey.giveMeKotlinBuilder<AccountMetaData>()
                .set(AccountMetaData::profileImage, "https://example.com/profile.jpg")
                .sample()

            every { accountInfoRepository.get() } returns flowOf(accountInfo)
            every { accountMetaDataRepository.get() } returns flowOf(accountMetaData)

            When("GetAccountUseCase를 호출하면") {
                val result = useCase().first()

                Then("Account.User를 반환한다") {
                    result.shouldBeSuccess()
                        .shouldBeInstanceOf<Account.User>()
                        .should {
                            it.accountId shouldBe accountInfo.id
                            it.email shouldBe accountInfo.email
                            it.profileImage shouldBe accountMetaData.profileImage
                        }
                }

                Then("AccountInfoRepository를 호출한다") {
                    verify(exactly = 1) { accountInfoRepository.get() }
                }

                Then("AccountMetaDataRepository를 호출한다") {
                    verify(exactly = 1) { accountMetaDataRepository.get() }
                }
            }
        }

        Given("AccountInfo가 존재하고 AccountMetaData가 null") {
            clearAllMocks()
            val accountInfo = fixtureMonkey.giveMeOne<AccountInfo>()

            every { accountInfoRepository.get() } returns flowOf(accountInfo)
            every { accountMetaDataRepository.get() } returns flowOf(null)

            When("GetAccountUseCase를 호출하면") {
                val result = useCase().first()

                Then("profileImage가 null인 Account.User를 반환한다") {
                    result.shouldBeSuccess()
                        .shouldBeInstanceOf<Account.User>()
                        .should {
                            it.accountId shouldBe accountInfo.id
                            it.email shouldBe accountInfo.email
                            it.profileImage shouldBe null
                        }
                }
            }
        }
    }
}
