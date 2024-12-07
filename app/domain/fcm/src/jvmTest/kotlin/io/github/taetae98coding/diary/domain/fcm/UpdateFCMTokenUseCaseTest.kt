package io.github.taetae98coding.diary.domain.fcm

import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.fcm.repository.FCMRepository
import io.github.taetae98coding.diary.domain.fcm.usecase.UpdateFCMTokenUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

class UpdateFCMTokenUseCaseTest : BehaviorSpec() {
	private val getAccountUseCase = mockk<GetAccountUseCase>()
	private val fcmRepository = mockk<FCMRepository>(relaxed = true, relaxUnitFun = true)
	private val useCase = UpdateFCMTokenUseCase(
		getAccountUseCase = getAccountUseCase,
		repository = fcmRepository,
	)

	init {
		Given("account is Member") {
			every { getAccountUseCase() } returns flowOf(Result.success(mockk<Account.Member>()))

			When("call useCase") {
				val result = useCase()

				Then("result is success") {
					result.shouldBeSuccess()
				}

				Then("do upsert") {
					coVerify { fcmRepository.upsert() }
				}

				clearAllMocks()
			}
		}

		Given("account is Guest") {
			every { getAccountUseCase() } returns flowOf(Result.success(mockk<Account.Guest>()))

			When("call useCase") {
				val result = useCase()

				Then("result is success") {
					result.shouldBeSuccess()
				}

				Then("do delete") {
					coVerify { fcmRepository.delete() }
				}

				clearAllMocks()
			}
		}
	}
}
