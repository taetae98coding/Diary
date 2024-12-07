package io.github.taetae98coding.diary.domain.account

import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.domain.account.repository.AccountRepository
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class GetAccountUseCaseTest : BehaviorSpec() {
	private val accountRepository = mockk<AccountRepository>()
	private val useCase = GetAccountUseCase(
		repository = accountRepository,
	)

	init {

		Given("no uid and email") {
			every { accountRepository.getUid() } returns flowOf(null)
			every { accountRepository.getEmail() } returns flowOf(null)

			When("call useCase") {
				val result = useCase().first()

				Then("result is guest") {
					result.shouldBeSuccess(mockk<Account.Guest>())
				}
			}

			clearAllMocks()
		}

		Given("has uid and email") {
			val account = mockk<Account.Member> {
				every { uid } returns "uid"
				every { email } returns "email"
			}

			every { accountRepository.getUid() } returns flowOf(account.uid)
			every { accountRepository.getEmail() } returns flowOf(account.email)

			When("call useCase") {
				val result = useCase().first()

				Then("result is member") {
					result.shouldBeSuccess(account)
				}
			}

			clearAllMocks()
		}
	}
}
