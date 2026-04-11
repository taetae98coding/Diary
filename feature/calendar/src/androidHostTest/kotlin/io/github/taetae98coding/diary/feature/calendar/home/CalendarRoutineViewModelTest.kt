package io.github.taetae98coding.diary.feature.calendar.home

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.github.taetae98coding.diary.core.model.routine.CalendarRoutine
import io.github.taetae98coding.diary.domain.routine.usecase.GetCalendarRoutineUseCase
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.YearMonth
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class CalendarRoutineViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getCalendarRoutineUseCase: GetCalendarRoutineUseCase
    private lateinit var viewModel: CalendarRoutineViewModel

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    private fun giveMeCalendarRoutineList(count: Int): List<CalendarRoutine> {
        val date = fixtureMonkey.giveMeKotlinBuilder<LocalDate>().sample()

        return (1..count).map {
            fixtureMonkey.giveMeKotlinBuilder<CalendarRoutine>()
                .set(CalendarRoutine::localDateRange, date..date)
                .sample()
        }
    }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        clearAllMocks()
        getCalendarRoutineUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `초기 상태는 빈 리스트이다`() {
        viewModel = CalendarRoutineViewModel(getCalendarRoutineUseCase)
        viewModel.calendarRoutine.value shouldBe emptyList()
    }

    @Test
    fun `fetch 시 해당 연도의 루틴을 가져온다`() = runTest(testDispatcher) {
        val routineList = giveMeCalendarRoutineList(5)

        every { getCalendarRoutineUseCase(2026) } returns flowOf(Result.success(routineList))

        viewModel = CalendarRoutineViewModel(getCalendarRoutineUseCase)
        backgroundScope.launch { viewModel.calendarRoutine.collect {} }
        viewModel.fetchYearMonth(YearMonth(2026, Month.MARCH))

        advanceUntilIdle()
        viewModel.calendarRoutine.value shouldBe routineList
    }

    @Test
    fun `1월 fetch 시 이전 연도도 함께 가져온다`() = runTest(testDispatcher) {
        val prevYearRoutines = giveMeCalendarRoutineList(5)
        val currentYearRoutines = giveMeCalendarRoutineList(5)

        every { getCalendarRoutineUseCase(2025) } returns flowOf(Result.success(prevYearRoutines))
        every { getCalendarRoutineUseCase(2026) } returns flowOf(Result.success(currentYearRoutines))

        viewModel = CalendarRoutineViewModel(getCalendarRoutineUseCase)
        backgroundScope.launch { viewModel.calendarRoutine.collect {} }
        viewModel.fetchYearMonth(YearMonth(2026, Month.JANUARY))

        advanceUntilIdle()
        viewModel.calendarRoutine.value shouldBe prevYearRoutines + currentYearRoutines
    }

    @Test
    fun `12월 fetch 시 다음 연도도 함께 가져온다`() = runTest(testDispatcher) {
        val currentYearRoutines = giveMeCalendarRoutineList(5)
        val nextYearRoutines = giveMeCalendarRoutineList(5)

        every { getCalendarRoutineUseCase(2025) } returns flowOf(Result.success(currentYearRoutines))
        every { getCalendarRoutineUseCase(2026) } returns flowOf(Result.success(nextYearRoutines))

        viewModel = CalendarRoutineViewModel(getCalendarRoutineUseCase)
        backgroundScope.launch { viewModel.calendarRoutine.collect {} }
        viewModel.fetchYearMonth(YearMonth(2025, Month.DECEMBER))

        advanceUntilIdle()
        viewModel.calendarRoutine.value shouldBe currentYearRoutines + nextYearRoutines
    }

    @Test
    fun `1월, 12월이 아닌 달은 해당 연도만 가져온다`() = runTest(testDispatcher) {
        val routineList = giveMeCalendarRoutineList(5)

        every { getCalendarRoutineUseCase(2026) } returns flowOf(Result.success(routineList))

        viewModel = CalendarRoutineViewModel(getCalendarRoutineUseCase)
        backgroundScope.launch { viewModel.calendarRoutine.collect {} }
        viewModel.fetchYearMonth(YearMonth(2026, Month.JUNE))

        advanceUntilIdle()
        verify(exactly = 1) { getCalendarRoutineUseCase(2026) }
        verify(exactly = 0) { getCalendarRoutineUseCase(2025) }
    }
}
