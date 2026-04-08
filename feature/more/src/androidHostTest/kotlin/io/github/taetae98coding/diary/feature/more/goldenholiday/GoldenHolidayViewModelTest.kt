package io.github.taetae98coding.diary.feature.more.goldenholiday

import io.github.taetae98coding.diary.core.model.holiday.Holiday
import io.github.taetae98coding.diary.domain.holiday.usecase.FetchHolidayUseCase
import io.github.taetae98coding.diary.domain.holiday.usecase.GetGoldenHolidayUseCase
import io.github.taetae98coding.diary.domain.holiday.usecase.GetHolidayUseCase
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class GoldenHolidayViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private val fetchHolidayUseCase = mockk<FetchHolidayUseCase>()
    private val getHolidayUseCase = mockk<GetHolidayUseCase>()
    private val getGoldenHolidayUseCase = mockk<GetGoldenHolidayUseCase>()
    private lateinit var viewModel: GoldenHolidayViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        clearAllMocks()
        viewModel = GoldenHolidayViewModel(2026, fetchHolidayUseCase, getHolidayUseCase, getGoldenHolidayUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `초기 상태는 Idle이다`() {
        viewModel.uiState.value.shouldBeInstanceOf<GoldenHolidayScaffoldUiState.Idle>()
    }

    @Test
    fun `fetch 성공 시 State를 반환한다`() = runTest {
        val holidays = listOf(mockk<Holiday>())
        val goldenHolidays = listOf(mockk<io.github.taetae98coding.diary.core.model.holiday.GoldenHoliday>())

        coEvery { fetchHolidayUseCase(2026) } returns Result.success(Unit)
        coEvery { getHolidayUseCase(2026) } returns flowOf(Result.success(holidays))
        coEvery { getGoldenHolidayUseCase(2026, 3) } returns flowOf(Result.success(goldenHolidays))

        viewModel.fetch(3)

        val state = viewModel.uiState.value
        state.shouldBeInstanceOf<GoldenHolidayScaffoldUiState.State>()
        state.annualLeave shouldBe 3
        state.goldenHolidayList shouldBe goldenHolidays
    }

    @Test
    fun `fetch 실패와 공휴일 없으면 UnknownError`() = runTest {
        coEvery { fetchHolidayUseCase(2026) } returns Result.failure(RuntimeException())
        coEvery { getHolidayUseCase(2026) } returns flowOf(Result.success(emptyList()))

        viewModel.fetch(3)

        viewModel.uiState.value.shouldBeInstanceOf<GoldenHolidayScaffoldUiState.UnknownError>()
    }

    @Test
    fun `fetch 성공이지만 공휴일 없으면 HolidayNotExist`() = runTest {
        coEvery { fetchHolidayUseCase(2026) } returns Result.success(Unit)
        coEvery { getHolidayUseCase(2026) } returns flowOf(Result.success(emptyList()))

        viewModel.fetch(3)

        viewModel.uiState.value.shouldBeInstanceOf<GoldenHolidayScaffoldUiState.HolidayNotExist>()
    }

    @Test
    fun `같은 annualLeave로 다시 fetch하면 무시한다`() = runTest {
        val holidays = listOf(mockk<Holiday>())

        coEvery { fetchHolidayUseCase(2026) } returns Result.success(Unit)
        coEvery { getHolidayUseCase(2026) } returns flowOf(Result.success(holidays))
        coEvery { getGoldenHolidayUseCase(2026, 3) } returns flowOf(Result.success(emptyList()))

        viewModel.fetch(3)
        val firstState = viewModel.uiState.value

        viewModel.fetch(3)
        viewModel.uiState.value shouldBe firstState
    }
}
