package io.github.taetae98coding.diary.domain.credential

import io.github.taetae98coding.diary.domain.credential.repository.CredentialRepository
import io.github.taetae98coding.diary.domain.credential.usecase.LoginUseCase
import io.github.taetae98coding.diary.domain.fcm.usecase.UpdateFCMTokenUseCase
import io.github.taetae98coding.diary.domain.fetch.usecase.FetchUseCase
import io.kotest.core.concurrency.CoroutineDispatcherFactory
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.withContext

class LoginUseCaseTest : BehaviorSpec() {
	private val dispatcher = UnconfinedTestDispatcher()
	private val coroutineScope = CoroutineScope(dispatcher)
	private val fetchUseCase = mockk<FetchUseCase>(relaxed = true, relaxUnitFun = true)
	private val updateFCMTokenUseCase = mockk<UpdateFCMTokenUseCase>(relaxed = true, relaxUnitFun = true)
	private val credentialRepository = mockk<CredentialRepository>(relaxed = true, relaxUnitFun = true)
	private val useCase = LoginUseCase(
		coroutineScope = coroutineScope,
		fetchUseCase = fetchUseCase,
		updateFCMTokenUseCase = updateFCMTokenUseCase,
		repository = credentialRepository,
	)

	init {
		coroutineDispatcherFactory = object : CoroutineDispatcherFactory {
			override suspend fun <T> withDispatcher(
				testCase: TestCase,
				f: suspend () -> T,
			): T = withContext(dispatcher) {
				f()
			}
		}

		Given("fetchToken is success") {
			every { credentialRepository.fetchToken(any(), any()) } returns flowOf(mockk(relaxed = true, relaxUnitFun = true))

			When("call useCase") {
				val email = "email"
				val password = "password"
				val result = useCase(email, password)

				Then("result is success") {
					result.shouldBeSuccess()
				}

				Then("did fetch") {
					coVerifyOrder {
						credentialRepository.fetchToken(email, password)
						credentialRepository.save(email, any())
						fetchUseCase()
					}
				}

				Then("did updateFCMToken") {
					coVerifyOrder {
						credentialRepository.fetchToken(email, password)
						credentialRepository.save(email, any())
						fetchUseCase()
					}
				}
			}
		}
	}
}
