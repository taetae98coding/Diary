package io.github.taetae98coding.diary.feature.routine.add.component

import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.v2.createComposeRule
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import io.github.taetae98coding.diary.core.model.routine.RRuleDiaryByDay
import kotlinx.datetime.DayOfWeek
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class ByDayEditorStateTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `기본값은 비어있다`() {
        val state = ByDayEditorState()
        assertEquals(emptySet<DayOfWeek>(), state.selectedDays)
        assertNull(state.ordinal)
        assertEquals(RRuleDiaryByDay(), state.diaryByDay)
    }

    @Test
    fun `selectDay로 선택 요일이 추가된다`() {
        val state = ByDayEditorState()

        state.selectDay(DayOfWeek.MONDAY)
        state.selectDay(DayOfWeek.WEDNESDAY)

        assertEquals(setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY), state.selectedDays)
    }

    @Test
    fun `unselectDay로 선택 요일이 제거된다`() {
        val state = ByDayEditorState(
            initialDiaryByDay = RRuleDiaryByDay(days = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)),
        )

        state.unselectDay(DayOfWeek.MONDAY)

        assertEquals(setOf(DayOfWeek.WEDNESDAY), state.selectedDays)
    }

    @Test
    fun `updateOrdinal로 ordinal이 변경된다`() {
        val state = ByDayEditorState()

        state.updateOrdinal(3)
        assertEquals(3, state.ordinal)

        state.updateOrdinal(null)
        assertNull(state.ordinal)
    }

    @Test
    fun `byDay는 selectedDays와 ordinal을 합쳐서 만든다`() {
        val state = ByDayEditorState()
        state.selectDay(DayOfWeek.MONDAY)
        state.selectDay(DayOfWeek.WEDNESDAY)
        state.updateOrdinal(2)

        assertEquals(
            RRuleDiaryByDay(days = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY), ordinal = 2),
            state.diaryByDay,
        )
    }

    @Test
    fun `updateByDay는 selectedDays와 ordinal을 동시에 갱신한다`() {
        val state = ByDayEditorState()

        state.updateDiaryByDay(
            RRuleDiaryByDay(days = setOf(DayOfWeek.FRIDAY, DayOfWeek.SUNDAY), ordinal = 4),
        )

        assertEquals(setOf(DayOfWeek.FRIDAY, DayOfWeek.SUNDAY), state.selectedDays)
        assertEquals(4, state.ordinal)
    }

    @Test
    fun `초기값으로부터 selectedDays와 ordinal이 추출된다`() {
        val state = ByDayEditorState(
            initialDiaryByDay = RRuleDiaryByDay(days = setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY), ordinal = 3),
        )

        assertEquals(setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY), state.selectedDays)
        assertEquals(3, state.ordinal)
    }

    @Test
    fun `byDay 라운드트립이 무손실이다`() {
        val original = RRuleDiaryByDay(days = setOf(DayOfWeek.MONDAY, DayOfWeek.FRIDAY), ordinal = 2)
        val state = ByDayEditorState(initialDiaryByDay = original)

        assertEquals(original, state.diaryByDay)
    }

    @Test
    fun `configuration change 후 selectedDays와 ordinal이 유지된다`() {
        lateinit var state: ByDayEditorState
        val restorationTester = StateRestorationTester(composeTestRule)

        restorationTester.setContent {
            DiaryTheme {
                state = rememberByDayEditorState()
            }
        }

        composeTestRule.runOnIdle {
            state.selectDay(DayOfWeek.MONDAY)
            state.selectDay(DayOfWeek.WEDNESDAY)
            state.updateOrdinal(2)
        }

        restorationTester.emulateSavedInstanceStateRestore()

        composeTestRule.runOnIdle {
            assertEquals(setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY), state.selectedDays)
            assertEquals(2, state.ordinal)
        }
    }

    @Test
    fun `configuration change 후 ordinal null도 유지된다`() {
        lateinit var state: ByDayEditorState
        val restorationTester = StateRestorationTester(composeTestRule)

        restorationTester.setContent {
            DiaryTheme {
                state = rememberByDayEditorState()
            }
        }

        composeTestRule.runOnIdle {
            state.selectDay(DayOfWeek.FRIDAY)
        }

        restorationTester.emulateSavedInstanceStateRestore()

        composeTestRule.runOnIdle {
            assertEquals(setOf(DayOfWeek.FRIDAY), state.selectedDays)
            assertNull(state.ordinal)
        }
    }
}
