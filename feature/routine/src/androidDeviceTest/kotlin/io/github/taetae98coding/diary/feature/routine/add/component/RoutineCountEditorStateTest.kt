package io.github.taetae98coding.diary.feature.routine.add.component

import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.v2.createComposeRule
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class RoutineCountEditorStateTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `기본 초기값은 1이다`() {
        val state = RoutineCountEditorState()
        assertEquals(1, state.count)
    }

    @Test
    fun `초기값이 1보다 작아도 1로 보정된다`() {
        val state = RoutineCountEditorState(initialCount = 0)
        assertEquals(1, state.count)
    }

    @Test
    fun `updateCount는 1 이상으로 보정된다`() {
        val state = RoutineCountEditorState(initialCount = 5)

        state.updateCount(0)

        assertEquals(1, state.count)
    }

    @Test
    fun `increment는 1 증가시킨다`() {
        val state = RoutineCountEditorState(initialCount = 3)

        state.increment()

        assertEquals(4, state.count)
    }

    @Test
    fun `decrement는 1 감소시키되 최소 1을 유지한다`() {
        val state = RoutineCountEditorState(initialCount = 2)

        state.decrement()
        assertEquals(1, state.count)

        state.decrement()
        assertEquals(1, state.count)
    }

    @Test
    fun `configuration change 후 count가 유지된다`() {
        lateinit var state: RoutineCountEditorState
        val restorationTester = StateRestorationTester(composeTestRule)

        restorationTester.setContent {
            DiaryTheme {
                state = rememberRoutineCountEditorState()
            }
        }

        composeTestRule.runOnIdle {
            state.updateCount(7)
        }

        restorationTester.emulateSavedInstanceStateRestore()

        composeTestRule.runOnIdle {
            assertEquals(7, state.count)
        }
    }
}
