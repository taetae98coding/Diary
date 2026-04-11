package io.github.taetae98coding.diary.feature.routine.add.card

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kotlinx.datetime.DayOfWeek
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ByDaySelectorStateTest {
    private lateinit var state: ByDaySelectorState

    @Before
    fun setup() {
        state = ByDaySelectorState()
    }

    @Test
    fun `초기 상태에서 selectedDaysOfWeek는 비어있다`() {
        state.selectedDaysOfWeek.shouldBeEmpty()
    }

    @Test
    fun `addDayOfWeek로 요일을 추가할 수 있다`() {
        state.addDayOfWeek(DayOfWeek.MONDAY)
        state.selectedDaysOfWeek shouldContainExactly setOf(DayOfWeek.MONDAY)
    }

    @Test
    fun `removeDayOfWeek로 요일을 제거할 수 있다`() {
        state.addDayOfWeek(DayOfWeek.MONDAY)
        state.addDayOfWeek(DayOfWeek.FRIDAY)
        state.removeDayOfWeek(DayOfWeek.MONDAY)
        state.selectedDaysOfWeek shouldContainExactly setOf(DayOfWeek.FRIDAY)
    }

    @Test
    fun `clear로 모든 요일을 제거할 수 있다`() {
        state.addDayOfWeek(DayOfWeek.MONDAY)
        state.addDayOfWeek(DayOfWeek.FRIDAY)
        state.clear()
        state.selectedDaysOfWeek.shouldBeEmpty()
    }

    @Test
    fun `selectedOrdinal 초기값은 null이다`() {
        state.selectedOrdinal shouldBe null
    }

    @Test
    fun `updateSelectedOrdinal로 ordinal을 설정하면 expanded가 false가 된다`() {
        state.updateExpanded(true)
        state.updateSelectedOrdinal(2)
        state.selectedOrdinal shouldBe 2
        state.expanded shouldBe false
    }

    @Test
    fun `updateExpanded로 확장 상태를 변경할 수 있다`() {
        state.updateExpanded(true)
        state.expanded shouldBe true
    }
}
