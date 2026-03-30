package io.github.taetae98coding.diary.presenter.calendar.api

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.KotlinTypeDefaultArbitraryBuilder
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.github.taetae98coding.diary.core.model.memo.CalendarMemo
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.YearMonth

class CalendarMemoStateHolderTest : FunSpec() {
    private val testDispatcher = StandardTestDispatcher()
    lateinit var testScope: TestScope
    lateinit var strategy: CalendarMemoStrategy
    lateinit var stateHolder: CalendarMemoStateHolder

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    private fun giveMeCalendarMemoBuilder(): KotlinTypeDefaultArbitraryBuilder<CalendarMemo> {
        val start = fixtureMonkey.giveMeKotlinBuilder<LocalDateTime>().sample()
        val endInclusive = fixtureMonkey.giveMeKotlinBuilder<LocalDateTime>().sample()

        return fixtureMonkey.giveMeKotlinBuilder<CalendarMemo>()
            .set(CalendarMemo::localDateTimeRange, minOf(start, endInclusive)..maxOf(start, endInclusive))
    }

    init {
        beforeTest {
            clearAllMocks()

            testScope = TestScope()
            strategy = mockk()
            stateHolder = CalendarMemoStateHolder(testScope, strategy)
        }

        test("초기 상태는 빈 리스트이다") {
            testScope.backgroundScope.launch { stateHolder.calendarMemo.collect {} }
            stateHolder.calendarMemo.value shouldBe emptyList()
        }

        test("fetch 시 해당 연도의 메모를 가져온다") {
            val memoList = giveMeCalendarMemoBuilder().sampleList(5)

            every { strategy.get(2026) } returns flowOf(Result.success(memoList))

            testScope.backgroundScope.launch { stateHolder.calendarMemo.collect {} }
            stateHolder.fetch(YearMonth(2026, Month.MARCH))

            testScope.advanceUntilIdle()
            stateHolder.calendarMemo.value shouldBe memoList
        }

        test("1월 fetch 시 이전 연도도 함께 가져온다") {
            val prevYearMemos = giveMeCalendarMemoBuilder().sampleList(5)
            val currentYearMemos = giveMeCalendarMemoBuilder().sampleList(5)

            every { strategy.get(2025) } returns flowOf(Result.success(prevYearMemos))
            every { strategy.get(2026) } returns flowOf(Result.success(currentYearMemos))

            testScope.backgroundScope.launch { stateHolder.calendarMemo.collect {} }
            stateHolder.fetch(YearMonth(2026, Month.JANUARY))

            testScope.advanceUntilIdle()
            stateHolder.calendarMemo.value shouldBe prevYearMemos + currentYearMemos
        }

        test("12월 fetch 시 이전 연도도 함께 가져온다") {
            val prevYearMemos = giveMeCalendarMemoBuilder().sampleList(5)
            val currentYearMemos = giveMeCalendarMemoBuilder().sampleList(5)

            every { strategy.get(2025) } returns flowOf(Result.success(prevYearMemos))
            every { strategy.get(2026) } returns flowOf(Result.success(currentYearMemos))

            testScope.backgroundScope.launch { stateHolder.calendarMemo.collect {} }
            stateHolder.fetch(YearMonth(2026, Month.DECEMBER))

            testScope.advanceUntilIdle()
            stateHolder.calendarMemo.value shouldBe currentYearMemos + prevYearMemos
        }

        test("1월, 12월이 아닌 달은 해당 연도만 가져온다") {
            val memoList = giveMeCalendarMemoBuilder().sampleList(5)

            every { strategy.get(2026) } returns flowOf(Result.success(memoList))

            testScope.backgroundScope.launch { stateHolder.calendarMemo.collect {} }
            stateHolder.fetch(YearMonth(2026, Month.JUNE))

            testScope.advanceUntilIdle()
            verify(exactly = 1) { strategy.get(2026) }
            verify(exactly = 0) { strategy.get(2025) }
        }
    }
}
