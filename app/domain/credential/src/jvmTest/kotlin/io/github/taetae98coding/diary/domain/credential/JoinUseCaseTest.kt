package io.github.taetae98coding.diary.domain.credential

import io.github.taetae98coding.diary.common.exception.account.InvalidEmailException
import io.github.taetae98coding.diary.domain.credential.repository.CredentialRepository
import io.github.taetae98coding.diary.domain.credential.usecase.JoinUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.Called
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify

class JoinUseCaseTest : BehaviorSpec() {
	private val credentialRepository = mockk<CredentialRepository>(relaxed = true, relaxUnitFun = true)
	private val useCase = JoinUseCase(
		repository = credentialRepository,
	)

	init {
		Given("invalid email") {
			val email = "email"
			val password = "password"

			When("call useCase") {
				val result = useCase(email, password)

				Then("result throw InvalidEmailException") {
					result.shouldBeFailure<InvalidEmailException>()
				}

				Then("do not join") {
					verify { credentialRepository wasNot Called }
				}

				clearAllMocks()
			}
		}

		Given("valid email") {
			val email = "1@1.1"
			val password = "password"

			When("call useCase") {
				val result = useCase(email, password)

				Then("result is success") {
					result.shouldBeSuccess()
				}

				Then("do join") {
					coVerify { credentialRepository.join(email, password) }
				}
			}
		}
	}
}
