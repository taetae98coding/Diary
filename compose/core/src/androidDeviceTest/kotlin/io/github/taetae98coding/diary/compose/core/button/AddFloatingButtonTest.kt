package io.github.taetae98coding.diary.compose.core.button

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.performClick
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class AddFloatingButtonTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `버튼이 표시된다`() {
        composeTestRule.setContent {
            DiaryTheme {
                AddFloatingButton(onClick = {})
            }
        }

        composeTestRule.onNode(hasClickAction()).assertIsDisplayed()
    }

    @Test
    fun `클릭하면 onClick이 호출된다`() {
        var clicked = false

        composeTestRule.setContent {
            DiaryTheme {
                AddFloatingButton(onClick = { clicked = true })
            }
        }

        composeTestRule.onNode(hasClickAction()).performClick()
        assertTrue(clicked)
    }

    @Test
    fun `진행 중이 아니면 프로그레스가 표시되지 않는다`() {
        composeTestRule.setContent {
            DiaryTheme {
                AddFloatingButton(onClick = {}, isInProgressProvider = { false })
            }
        }

        composeTestRule.onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate), useUnmergedTree = true).assertDoesNotExist()
    }

    @Test
    fun `진행 중이면 프로그레스가 표시된다`() {
        composeTestRule.setContent {
            DiaryTheme {
                AddFloatingButton(onClick = {}, isInProgressProvider = { true })
            }
        }

        composeTestRule.onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate), useUnmergedTree = true).assertIsDisplayed()
    }
}
