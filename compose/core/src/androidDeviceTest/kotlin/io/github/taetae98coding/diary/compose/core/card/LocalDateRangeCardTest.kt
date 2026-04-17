package io.github.taetae98coding.diary.compose.core.card

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import kotlinx.datetime.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class LocalDateRangeCardTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `날짜 범위 타이틀과 시작일 종료일 라벨이 표시된다`() {
        composeTestRule.setContent {
            DiaryTheme {
                LocalDateRangeCard()
            }
        }

        composeTestRule.onNodeWithText("날짜 범위").assertIsDisplayed()
        composeTestRule.onNodeWithText("시작일").assertIsDisplayed()
        composeTestRule.onNodeWithText("종료일").assertIsDisplayed()
    }

    @Test
    fun `초기 값이 null이면 없음이 표시된다`() {
        composeTestRule.setContent {
            DiaryTheme {
                LocalDateRangeCard()
            }
        }

        composeTestRule.onAllNodesWithText("없음")[0].assertIsDisplayed()
        composeTestRule.onAllNodesWithText("없음")[1].assertIsDisplayed()
    }

    @Test
    fun `초기 값이 null이면 Clear 버튼이 표시되지 않는다`() {
        composeTestRule.setContent {
            DiaryTheme {
                LocalDateRangeCard()
            }
        }

        composeTestRule.onNodeWithTag(LOCAL_DATE_RANGE_CARD_START_CLEAR_TEST_TAG).assertDoesNotExist()
        composeTestRule.onNodeWithTag(LOCAL_DATE_RANGE_CARD_END_CLEAR_TEST_TAG).assertDoesNotExist()
    }

    @Test
    fun `초기 값이 있으면 Clear 버튼이 표시된다`() {
        composeTestRule.setContent {
            DiaryTheme {
                LocalDateRangeCard(
                    state = rememberLocalDateRangeCardState(
                        initialStart = LocalDate(2026, 4, 17),
                        initialEndInclusive = LocalDate(2026, 4, 20),
                    ),
                )
            }
        }

        composeTestRule.onNodeWithTag(LOCAL_DATE_RANGE_CARD_START_CLEAR_TEST_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithTag(LOCAL_DATE_RANGE_CARD_END_CLEAR_TEST_TAG).assertIsDisplayed()
    }

    @Test
    fun `시작일 Clear 버튼을 누르면 start가 null이 된다`() {
        lateinit var state: LocalDateRangeCardState

        composeTestRule.setContent {
            DiaryTheme {
                state = rememberLocalDateRangeCardState(
                    initialStart = LocalDate(2026, 4, 17),
                    initialEndInclusive = LocalDate(2026, 4, 20),
                )
                LocalDateRangeCard(state = state)
            }
        }

        composeTestRule.onNodeWithTag(LOCAL_DATE_RANGE_CARD_START_CLEAR_TEST_TAG).performClick()

        composeTestRule.runOnIdle {
            assertNull(state.start)
            assertEquals(LocalDate(2026, 4, 20), state.endInclusive)
        }
    }

    @Test
    fun `종료일 Clear 버튼을 누르면 endInclusive가 null이 된다`() {
        lateinit var state: LocalDateRangeCardState

        composeTestRule.setContent {
            DiaryTheme {
                state = rememberLocalDateRangeCardState(
                    initialStart = LocalDate(2026, 4, 17),
                    initialEndInclusive = LocalDate(2026, 4, 20),
                )
                LocalDateRangeCard(state = state)
            }
        }

        composeTestRule.onNodeWithTag(LOCAL_DATE_RANGE_CARD_END_CLEAR_TEST_TAG).performClick()

        composeTestRule.runOnIdle {
            assertEquals(LocalDate(2026, 4, 17), state.start)
            assertNull(state.endInclusive)
        }
    }

    @Test
    fun `start가 endInclusive보다 크면 endInclusive가 start로 자동 변경된다`() {
        val state = LocalDateRangeCardState(
            initialStart = LocalDate(2026, 4, 17),
            initialEndInclusive = LocalDate(2026, 4, 20),
        )

        state.updateStart(LocalDate(2026, 4, 25))

        assertEquals(LocalDate(2026, 4, 25), state.start)
        assertEquals(LocalDate(2026, 4, 25), state.endInclusive)
    }

    @Test
    fun `endInclusive가 start보다 작으면 start가 endInclusive로 자동 변경된다`() {
        val state = LocalDateRangeCardState(
            initialStart = LocalDate(2026, 4, 17),
            initialEndInclusive = LocalDate(2026, 4, 20),
        )

        state.updateEndInclusive(LocalDate(2026, 4, 10))

        assertEquals(LocalDate(2026, 4, 10), state.start)
        assertEquals(LocalDate(2026, 4, 10), state.endInclusive)
    }

    @Test
    fun `endInclusive가 null이면 start 업데이트 시 endInclusive는 null로 유지된다`() {
        val state = LocalDateRangeCardState(
            initialStart = null,
            initialEndInclusive = null,
        )

        state.updateStart(LocalDate(2026, 4, 25))

        assertEquals(LocalDate(2026, 4, 25), state.start)
        assertNull(state.endInclusive)
    }

    @Test
    fun `start가 null이면 endInclusive 업데이트 시 start는 null로 유지된다`() {
        val state = LocalDateRangeCardState(
            initialStart = null,
            initialEndInclusive = null,
        )

        state.updateEndInclusive(LocalDate(2026, 4, 25))

        assertNull(state.start)
        assertEquals(LocalDate(2026, 4, 25), state.endInclusive)
    }

    @Test
    fun `configuration change 후 상태가 유지된다`() {
        val start = LocalDate(2026, 5, 1)
        val end = LocalDate(2026, 5, 10)
        lateinit var state: LocalDateRangeCardState

        val restorationTester = StateRestorationTester(composeTestRule)
        restorationTester.setContent {
            DiaryTheme {
                state = rememberLocalDateRangeCardState()
                LocalDateRangeCard(state = state)
            }
        }

        composeTestRule.runOnIdle {
            state.updateStart(start)
            state.updateEndInclusive(end)
        }

        restorationTester.emulateSavedInstanceStateRestore()

        composeTestRule.runOnIdle {
            assertEquals(start, state.start)
            assertEquals(end, state.endInclusive)
        }
    }
}
