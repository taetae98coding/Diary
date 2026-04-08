package io.github.taetae98coding.diary.feature.calendar.home

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.KotlinTypeDefaultArbitraryBuilder
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.github.taetae98coding.diary.core.model.memo.CalendarMemo
import io.github.taetae98coding.diary.domain.memo.usecase.GetCalendarMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.HasCalendarMemoFilterUseCase
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
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.YearMonth
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class CalendarMemoViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getCalendarMemoUseCase: GetCalendarMemoUseCase
    private lateinit var hasCalendarMemoFilterUseCase: HasCalendarMemoFilterUseCase
    private lateinit var viewModel: CalendarMemoViewModel

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    private fun giveMeCalendarMemoBuilder(): KotlinTypeDefaultArbitraryBuilder<CalendarMemo> {
        val start = fixtureMonkey.giveMeKotlinBuilder<LocalDateTime>().sample()
        val endInclusive = fixtureMonkey.giveMeKotlinBuilder<LocalDateTime>().sample()

        return fixtureMonkey.giveMeKotlinBuilder<CalendarMemo>()
            .set(CalendarMemo::localDateTimeRange, minOf(start, endInclusive)..maxOf(start, endInclusive))
    }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        clearAllMocks()
        getCalendarMemoUseCase = mockk()
        hasCalendarMemoFilterUseCase = mockk()
        every { hasCalendarMemoFilterUseCase() } returns flowOf(Result.success(false))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `초기 상태는 빈 리스트이다`() {
        viewModel = CalendarMemoViewModel(getCalendarMemoUseCase, hasCalendarMemoFilterUseCase)
        viewModel.calendarMemo.value shouldBe emptyList()
    }

    @Test
    fun `fetch 시 해당 연도의 메모를 가져온다`() = runTest(testDispatcher) {
        val memoList = giveMeCalendarMemoBuilder().sampleList(5)

        every { getCalendarMemoUseCase(2026) } returns flowOf(Result.success(memoList))

        viewModel = CalendarMemoViewModel(getCalendarMemoUseCase, hasCalendarMemoFilterUseCase)
        backgroundScope.launch { viewModel.calendarMemo.collect {} }
        viewModel.fetchYearMonth(YearMonth(2026, Month.MARCH))

        advanceUntilIdle()
        viewModel.calendarMemo.value shouldBe memoList
    }

    @Test
    fun `1월 fetch 시 이전 연도도 함께 가져온다`() = runTest(testDispatcher) {
        val prevYearMemos = giveMeCalendarMemoBuilder().sampleList(5)
        val currentYearMemos = giveMeCalendarMemoBuilder().sampleList(5)

        every { getCalendarMemoUseCase(2025) } returns flowOf(Result.success(prevYearMemos))
        every { getCalendarMemoUseCase(2026) } returns flowOf(Result.success(currentYearMemos))

        viewModel = CalendarMemoViewModel(getCalendarMemoUseCase, hasCalendarMemoFilterUseCase)
        backgroundScope.launch { viewModel.calendarMemo.collect {} }
        viewModel.fetchYearMonth(YearMonth(2026, Month.JANUARY))

        advanceUntilIdle()
        viewModel.calendarMemo.value shouldBe prevYearMemos + currentYearMemos
    }

    @Test
    fun `12월 fetch 시 다음 연도도 함께 가져온다`() = runTest(testDispatcher) {
        val currentYearMemos = giveMeCalendarMemoBuilder().sampleList(5)
        val nextYearMemos = giveMeCalendarMemoBuilder().sampleList(5)

        every { getCalendarMemoUseCase(2025) } returns flowOf(Result.success(currentYearMemos))
        every { getCalendarMemoUseCase(2026) } returns flowOf(Result.success(nextYearMemos))

        viewModel = CalendarMemoViewModel(getCalendarMemoUseCase, hasCalendarMemoFilterUseCase)
        backgroundScope.launch { viewModel.calendarMemo.collect {} }
        viewModel.fetchYearMonth(YearMonth(2025, Month.DECEMBER))

        advanceUntilIdle()
        viewModel.calendarMemo.value shouldBe currentYearMemos + nextYearMemos
    }

    @Test
    fun `1월, 12월이 아닌 달은 해당 연도만 가져온다`() = runTest(testDispatcher) {
        val memoList = giveMeCalendarMemoBuilder().sampleList(5)

        every { getCalendarMemoUseCase(2026) } returns flowOf(Result.success(memoList))

        viewModel = CalendarMemoViewModel(getCalendarMemoUseCase, hasCalendarMemoFilterUseCase)
        backgroundScope.launch { viewModel.calendarMemo.collect {} }
        viewModel.fetchYearMonth(YearMonth(2026, Month.JUNE))

        advanceUntilIdle()
        verify(exactly = 1) { getCalendarMemoUseCase(2026) }
        verify(exactly = 0) { getCalendarMemoUseCase(2025) }
    }
}
