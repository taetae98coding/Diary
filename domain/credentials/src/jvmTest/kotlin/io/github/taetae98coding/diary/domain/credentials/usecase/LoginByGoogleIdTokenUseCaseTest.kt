package io.github.taetae98coding.diary.domain.credentials.usecase

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import io.github.taetae98coding.diary.domain.credentials.repository.SessionRepository
import io.github.taetae98coding.diary.domain.sync.usecase.RequestSyncUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.mockk

class LoginByGoogleIdTokenUseCaseTest : BehaviorSpec() {
    private val sessionRepository = mockk<SessionRepository>()
    private val requestSyncUseCase = mockk<RequestSyncUseCase>(relaxed = true)
    private val useCase = LoginByGoogleIdTokenUseCase(sessionRepository, requestSyncUseCase)

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        Given("유효한 idToken") {
            clearAllMocks()
            val idToken = fixtureMonkey.giveMeOne<String>()

            coEvery { sessionRepository.updateByGoogleIdToken(idToken) } returns Unit
            coEvery { requestSyncUseCase() } returns Unit

            When("LoginByGoogleIdTokenUseCase를 호출하면") {
                val result = useCase(idToken)

                Then("성공한다") {
                    result.shouldBeSuccess()
                }

                Then("세션을 업데이트한 후 동기화를 요청한다") {
                    coVerifyOrder {
                        sessionRepository.updateByGoogleIdToken(idToken)
                        requestSyncUseCase()
                    }
                }
            }
        }

        Given("세션 업데이트가 실패하는 경우") {
            clearAllMocks()
            val idToken = fixtureMonkey.giveMeOne<String>()
            val exception = RuntimeException("session update failed")

            coEvery { sessionRepository.updateByGoogleIdToken(idToken) } throws exception

            When("LoginByGoogleIdTokenUseCase를 호출하면") {
                val result = useCase(idToken)

                Then("실패한다") {
                    result.shouldBeFailure<RuntimeException>()
                }

                Then("동기화를 요청하지 않는다") {
                    coVerify(exactly = 0) { requestSyncUseCase() }
                }
            }
        }
    }
}
