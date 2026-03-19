package io.github.taetae98coding.diary.feature.more.home

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.credentials.usecase.LogoutUseCase
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
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

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class MoreHomeAccountViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    private lateinit var accountFlow: MutableSharedFlow<Result<Account>>
    private lateinit var getAccountUseCase: GetAccountUseCase
    private lateinit var logoutUseCase: LogoutUseCase
    private lateinit var viewModel: MoreHomeAccountViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        clearAllMocks()
        accountFlow = MutableSharedFlow()
        getAccountUseCase = mockk { every { this@mockk() } returns accountFlow }
        logoutUseCase = mockk()
        viewModel = MoreHomeAccountViewModel(getAccountUseCase, logoutUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `초기 상태는 Loading`() {
        viewModel.uiState.value shouldBe MoreHomeAccountUiState.Loading
    }

    @Test
    fun `Guest 계정이면 NotLogin`() = runTest(testDispatcher) {
        backgroundScope.launch { viewModel.uiState.collect {} }
        runCurrent()

        accountFlow.emit(Result.success(Account.Guest))
        advanceUntilIdle()

        viewModel.uiState.value shouldBe MoreHomeAccountUiState.NotLogin
    }

    @Test
    fun `User 계정이면 Login`() = runTest(testDispatcher) {
        backgroundScope.launch { viewModel.uiState.collect {} }
        runCurrent()

        val user = fixtureMonkey.giveMeKotlinBuilder<Account.User>().sample()
        accountFlow.emit(Result.success(user))
        advanceUntilIdle()

        viewModel.uiState.value shouldBe MoreHomeAccountUiState.Login(
            email = user.email,
            profileImage = user.profileImage,
        )
    }

    @Test
    fun `계정 조회 실패 시 NotLogin`() = runTest(testDispatcher) {
        backgroundScope.launch { viewModel.uiState.collect {} }
        runCurrent()

        accountFlow.emit(Result.failure(RuntimeException()))
        advanceUntilIdle()

        viewModel.uiState.value shouldBe MoreHomeAccountUiState.NotLogin
    }

    @Test
    fun `logout 호출 시 logoutUseCase가 실행된다`() = runTest(testDispatcher) {
        coEvery { logoutUseCase() } returns Result.success(Unit)

        viewModel.logout()
        advanceUntilIdle()

        coVerify(exactly = 1) { logoutUseCase() }
    }

    @Test
    fun `logout 중에는 Loading 상태이다`() = runTest(testDispatcher) {
        backgroundScope.launch { viewModel.uiState.collect {} }
        runCurrent()

        val deferred = CompletableDeferred<Result<Unit>>()
        coEvery { logoutUseCase() } coAnswers { deferred.await() }

        accountFlow.emit(Result.success(Account.Guest))
        advanceUntilIdle()
        viewModel.uiState.value shouldBe MoreHomeAccountUiState.NotLogin

        viewModel.logout()
        runCurrent()
        viewModel.uiState.value shouldBe MoreHomeAccountUiState.Loading

        deferred.complete(Result.success(Unit))
        advanceUntilIdle()
        viewModel.uiState.value shouldBe MoreHomeAccountUiState.NotLogin
    }
}
