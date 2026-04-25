package io.github.taetae98coding.diary.feature.routine.add.component

import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.v2.createComposeRule
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ByMonthDayEditorStateTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `기본값은 비어있다`() {
        val state = ByMonthDayEditorState()
        assertEquals(emptySet<Int>(), state.byMonthDay)
        assertFalse(state.hasLastDay)
    }

    @Test
    fun `초기값을 보존한다`() {
        val state = ByMonthDayEditorState(initialByMonthDay = setOf(1, 15, 31))
        assertEquals(setOf(1, 15, 31), state.byMonthDay)
    }

    @Test
    fun `selectDay로 일자가 추가된다`() {
        val state = ByMonthDayEditorState()

        state.selectDay(15)
        state.selectDay(20)

        assertEquals(setOf(15, 20), state.byMonthDay)
    }

    @Test
    fun `unselectDay로 일자가 제거된다`() {
        val state = ByMonthDayEditorState(initialByMonthDay = setOf(15, 20))

        state.unselectDay(15)

        assertEquals(setOf(20), state.byMonthDay)
    }

    @Test
    fun `selectLastDay로 마지막 날이 선택된다`() {
        val state = ByMonthDayEditorState()

        state.selectLastDay()

        assertTrue(state.hasLastDay)
        assertEquals(setOf(-1), state.byMonthDay)
    }

    @Test
    fun `unselectLastDay로 마지막 날이 해제된다`() {
        val state = ByMonthDayEditorState(initialByMonthDay = setOf(-1, 15))

        state.unselectLastDay()

        assertFalse(state.hasLastDay)
        assertEquals(setOf(15), state.byMonthDay)
    }

    @Test
    fun `hasLastDay는 -1 포함 여부를 반영한다`() {
        val without = ByMonthDayEditorState(initialByMonthDay = setOf(15))
        val withLast = ByMonthDayEditorState(initialByMonthDay = setOf(-1))

        assertFalse(without.hasLastDay)
        assertTrue(withLast.hasLastDay)
    }

    @Test
    fun `updateByMonthDay로 통째로 교체된다`() {
        val state = ByMonthDayEditorState(initialByMonthDay = setOf(1))

        state.updateByMonthDay(setOf(10, 20, 30))

        assertEquals(setOf(10, 20, 30), state.byMonthDay)
    }

    @Test
    fun `configuration change 후 byMonthDay가 유지된다`() {
        lateinit var state: ByMonthDayEditorState
        val restorationTester = StateRestorationTester(composeTestRule)

        restorationTester.setContent {
            DiaryTheme {
                state = rememberByMonthDayEditorState()
            }
        }

        composeTestRule.runOnIdle {
            state.selectDay(7)
            state.selectDay(14)
            state.selectLastDay()
        }

        restorationTester.emulateSavedInstanceStateRestore()

        composeTestRule.runOnIdle {
            assertEquals(setOf(7, 14, -1), state.byMonthDay)
            assertTrue(state.hasLastDay)
        }
    }
}
