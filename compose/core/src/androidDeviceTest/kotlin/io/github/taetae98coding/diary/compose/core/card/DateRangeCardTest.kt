package io.github.taetae98coding.diary.compose.core.card

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import kotlinx.datetime.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class DateRangeCardTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `날짜 범위 텍스트가 표시된다`() {
        composeTestRule.setContent {
            DiaryTheme {
                DateRangeCard()
            }
        }

        composeTestRule.onNodeWithText("날짜 범위").assertIsDisplayed()
    }

    @Test
    fun `초기 상태에서 날짜 범위가 꺼져있으면 날짜 선택 UI가 표시되지 않는다`() {
        composeTestRule.setContent {
            DiaryTheme {
                DateRangeCard()
            }
        }

        composeTestRule.onAllNodesWithText("종일").assertCountEquals(0)
    }

    @Test
    fun `날짜 범위를 토글하면 날짜 선택 UI가 표시된다`() {
        composeTestRule.setContent {
            DiaryTheme {
                DateRangeCard()
            }
        }

        composeTestRule.onNodeWithText("날짜 범위").performClick()
        composeTestRule.onNodeWithText("종일").assertIsDisplayed()
    }

    @Test
    fun `초기 상태에서 종일이 켜져있으면 시간 선택 버튼이 표시되지 않는다`() {
        composeTestRule.setContent {
            DiaryTheme {
                DateRangeCard(
                    state = rememberDateRangeCardState(
                        initialHasDateRange = true,
                        initialStart = LocalDateTime(2026, 3, 27, 10, 0),
                        initialEndInclusive = LocalDateTime(2026, 3, 27, 11, 0),
                    ),
                )
            }
        }

        composeTestRule.onNodeWithText("종일").assertIsDisplayed()
        composeTestRule.onAllNodesWithText("오전").assertCountEquals(0)
        composeTestRule.onAllNodesWithText("오후").assertCountEquals(0)
    }

    @Test
    fun `종일을 토글하면 시간 선택 버튼이 표시된다`() {
        composeTestRule.setContent {
            DiaryTheme {
                DateRangeCard(
                    state = rememberDateRangeCardState(
                        initialHasDateRange = true,
                        initialStart = LocalDateTime(2026, 3, 27, 10, 0),
                        initialEndInclusive = LocalDateTime(2026, 3, 27, 11, 0),
                    ),
                )
            }
        }

        composeTestRule.onNodeWithText("종일").performClick()
        composeTestRule.onAllNodesWithText("오전")[0].assertIsDisplayed()
    }

    @Test
    fun `시작 날짜가 종료 날짜보다 크면 종료 날짜가 시작 날짜로 변경된다`() {
        val initialStart = LocalDateTime(2026, 3, 27, 10, 0)
        val initialEndInclusive = LocalDateTime(2026, 3, 27, 11, 0)
        val newStart = LocalDateTime(2026, 3, 28, 10, 0)

        val state = DateRangeCardState(
            initialHasDateRange = true,
            initialIsAllDay = true,
            initialStart = initialStart,
            initialEndInclusive = initialEndInclusive,
        )

        state.updateStart(newStart)

        assertEquals(newStart, state.endInclusive)
    }

    @Test
    fun `configuration change 후 상태가 유지된다`() {
        val updatedStart = LocalDateTime(2026, 4, 1, 9, 0)
        val updatedEndInclusive = LocalDateTime(2026, 4, 3, 18, 30)
        lateinit var state: DateRangeCardState

        val restorationTester = StateRestorationTester(composeTestRule)
        restorationTester.setContent {
            DiaryTheme {
                val state = rememberDateRangeCardState()
                    .also { state = it }

                DateRangeCard(state = state)
            }
        }

        // 날짜 범위 토글 ON
        composeTestRule.onNodeWithText("날짜 범위").performClick()
        // 종일 토글 OFF
        composeTestRule.onNodeWithText("종일").performClick()
        // 날짜 값 업데이트
        composeTestRule.runOnIdle {
            state.updateStart(updatedStart)
            state.updateEndInclusive(updatedEndInclusive)
        }

        restorationTester.emulateSavedInstanceStateRestore()

        // UI 검증
        composeTestRule.onNodeWithText("종일").assertIsDisplayed()
        composeTestRule.onAllNodesWithText("오전")[0].assertIsDisplayed()

        // state 값 검증
        assertTrue(state.hasDateRange)
        assertFalse(state.isAllDay)
        assertEquals(updatedStart, state.start)
        assertEquals(updatedEndInclusive, state.endInclusive)
    }
}
