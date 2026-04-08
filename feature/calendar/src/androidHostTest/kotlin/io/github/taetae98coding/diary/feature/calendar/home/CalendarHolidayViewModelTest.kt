package io.github.taetae98coding.diary.feature.calendar.home

import io.github.taetae98coding.diary.domain.holiday.usecase.FetchHolidayUseCase
import io.github.taetae98coding.diary.domain.holiday.usecase.GetHolidayUseCase
import io.kotest.matchers.collections.shouldBeEmpty
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.Month
import kotlinx.datetime.YearMonth
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class CalendarHolidayViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private val getHolidayUseCase = mockk<GetHolidayUseCase>()
    private val fetchHolidayUseCase = mockk<FetchHolidayUseCase>()
    private lateinit var viewModel: CalendarHolidayViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        clearAllMocks()
        viewModel = CalendarHolidayViewModel(getHolidayUseCase, fetchHolidayUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `초기 상태에서 holiday는 비어있다`() {
        viewModel.holiday.value.shouldBeEmpty()
    }

    @Test
    fun `6월 fetch 시 해당 연도만 fetch한다`() = runTest {
        every { getHolidayUseCase(2026) } returns flowOf(Result.success(emptyList()))
        coEvery { fetchHolidayUseCase(any()) } returns Result.success(Unit)

        viewModel.fetch(YearMonth(2026, Month.JUNE))

        coVerify(exactly = 1) { fetchHolidayUseCase(2026) }
        coVerify(exactly = 0) { fetchHolidayUseCase(2025) }
        coVerify(exactly = 0) { fetchHolidayUseCase(2027) }
    }

    @Test
    fun `1월 fetch 시 전년도도 함께 fetch한다`() = runTest {
        every { getHolidayUseCase(2025) } returns flowOf(Result.success(emptyList()))
        every { getHolidayUseCase(2026) } returns flowOf(Result.success(emptyList()))
        coEvery { fetchHolidayUseCase(any()) } returns Result.success(Unit)

        viewModel.fetch(YearMonth(2026, Month.JANUARY))

        coVerify(exactly = 1) { fetchHolidayUseCase(2025) }
        coVerify(exactly = 1) { fetchHolidayUseCase(2026) }
    }

    @Test
    fun `12월 fetch 시 익년도도 함께 fetch한다`() = runTest {
        every { getHolidayUseCase(2026) } returns flowOf(Result.success(emptyList()))
        every { getHolidayUseCase(2027) } returns flowOf(Result.success(emptyList()))
        coEvery { fetchHolidayUseCase(any()) } returns Result.success(Unit)

        viewModel.fetch(YearMonth(2026, Month.DECEMBER))

        coVerify(exactly = 1) { fetchHolidayUseCase(2026) }
        coVerify(exactly = 1) { fetchHolidayUseCase(2027) }
    }
}
