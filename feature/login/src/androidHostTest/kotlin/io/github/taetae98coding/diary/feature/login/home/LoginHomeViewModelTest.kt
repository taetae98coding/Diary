package io.github.taetae98coding.diary.feature.login.home

import io.github.taetae98coding.diary.core.google.credentials.api.GoogleCredentials
import io.github.taetae98coding.diary.domain.credentials.usecase.LoginByGoogleAuthorizationCodeUseCase
import io.github.taetae98coding.diary.domain.credentials.usecase.LoginByGoogleIdTokenUseCase
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LoginHomeViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var loginByGoogleAuthorizationCodeUseCase: LoginByGoogleAuthorizationCodeUseCase
    private lateinit var loginByGoogleIdTokenUseCase: LoginByGoogleIdTokenUseCase
    private lateinit var viewModel: LoginHomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        clearAllMocks()
        loginByGoogleAuthorizationCodeUseCase = mockk()
        loginByGoogleIdTokenUseCase = mockk()
        viewModel = LoginHomeViewModel(loginByGoogleAuthorizationCodeUseCase, loginByGoogleIdTokenUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `IdToken 로그인 성공 시 effect가 Finish`() = runTest(testDispatcher) {
        val credentials = GoogleCredentials.IdToken(idToken = "test-token")
        coEvery { loginByGoogleIdTokenUseCase(credentials.idToken) } returns Result.success(Unit)

        viewModel.login(credentials)
        advanceUntilIdle()

        viewModel.effect.value shouldBe LoginEffect.Finish
        viewModel.isInProgress.value shouldBe false
    }

    @Test
    fun `IdToken 로그인 실패 시 effect가 UnknownError`() = runTest(testDispatcher) {
        val credentials = GoogleCredentials.IdToken(idToken = "test-token")
        coEvery { loginByGoogleIdTokenUseCase(credentials.idToken) } returns Result.failure(RuntimeException())

        viewModel.login(credentials)
        advanceUntilIdle()

        viewModel.effect.value shouldBe LoginEffect.UnknownError
        viewModel.isInProgress.value shouldBe false
    }

    @Test
    fun `AuthorizationCode 로그인 성공 시 effect가 Finish`() = runTest(testDispatcher) {
        val credentials = GoogleCredentials.AuthorizationCode(
            clientId = "client-id",
            code = "auth-code",
            redirectUri = "redirect-uri",
        )
        coEvery {
            loginByGoogleAuthorizationCodeUseCase(credentials.clientId, credentials.code, credentials.redirectUri)
        } returns Result.success(Unit)

        viewModel.login(credentials)
        advanceUntilIdle()

        viewModel.effect.value shouldBe LoginEffect.Finish
        viewModel.isInProgress.value shouldBe false
    }

    @Test
    fun `AuthorizationCode 로그인 실패 시 effect가 UnknownError`() = runTest(testDispatcher) {
        val credentials = GoogleCredentials.AuthorizationCode(
            clientId = "client-id",
            code = "auth-code",
            redirectUri = "redirect-uri",
        )
        coEvery {
            loginByGoogleAuthorizationCodeUseCase(credentials.clientId, credentials.code, credentials.redirectUri)
        } returns Result.failure(RuntimeException())

        viewModel.login(credentials)
        advanceUntilIdle()

        viewModel.effect.value shouldBe LoginEffect.UnknownError
        viewModel.isInProgress.value shouldBe false
    }

    @Test
    fun `isInProgress 중이면 login을 무시한다`() = runTest(testDispatcher) {
        val credentials = GoogleCredentials.IdToken(idToken = "test-token")
        val deferred = CompletableDeferred<Result<Unit>>()
        coEvery { loginByGoogleIdTokenUseCase(credentials.idToken) } coAnswers { deferred.await() }

        viewModel.login(credentials)
        runCurrent()
        viewModel.isInProgress.value shouldBe true

        viewModel.login(credentials)

        deferred.complete(Result.success(Unit))
        advanceUntilIdle()

        coVerify(exactly = 1) { loginByGoogleIdTokenUseCase(credentials.idToken) }
    }

    @Test
    fun `consumeEffect 호출 시 effect가 None으로 초기화된다`() = runTest(testDispatcher) {
        val credentials = GoogleCredentials.IdToken(idToken = "test-token")
        coEvery { loginByGoogleIdTokenUseCase(credentials.idToken) } returns Result.success(Unit)

        viewModel.login(credentials)
        advanceUntilIdle()
        viewModel.consumeEffect()

        viewModel.effect.value shouldBe LoginEffect.None
    }
}
