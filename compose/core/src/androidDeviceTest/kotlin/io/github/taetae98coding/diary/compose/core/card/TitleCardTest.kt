package io.github.taetae98coding.diary.compose.core.card

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import io.github.taetae98coding.diary.compose.core.text.CLEAR_BUTTON_TEST_TAG
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class TitleCardTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `제목 라벨이 표시된다`() {
        composeTestRule.setContent {
            DiaryTheme {
                TitleCard()
            }
        }

        composeTestRule.onNodeWithText("제목").assertIsDisplayed()
    }

    @Test
    fun `초기 텍스트가 표시된다`() {
        val initialText = "초기 제목"

        composeTestRule.setContent {
            DiaryTheme {
                TitleCard(state = rememberTitleCardState(initialText = initialText))
            }
        }

        composeTestRule.onNodeWithText(initialText).assertIsDisplayed()
    }

    @Test
    fun `텍스트가 비어있으면 Clear 버튼이 표시되지 않는다`() {
        composeTestRule.setContent {
            DiaryTheme {
                TitleCard()
            }
        }

        composeTestRule.onNodeWithTag(CLEAR_BUTTON_TEST_TAG).assertDoesNotExist()
    }

    @Test
    fun `텍스트가 있으면 Clear 버튼이 표시된다`() {
        composeTestRule.setContent {
            DiaryTheme {
                TitleCard(state = rememberTitleCardState(initialText = "제목 텍스트"))
            }
        }

        composeTestRule.onNodeWithTag(CLEAR_BUTTON_TEST_TAG).assertIsDisplayed()
    }

    @Test
    fun `Clear 버튼을 클릭하면 텍스트가 초기화된다`() {
        val initialText = "삭제될 텍스트"

        composeTestRule.setContent {
            DiaryTheme {
                TitleCard(state = rememberTitleCardState(initialText = initialText))
            }
        }

        composeTestRule.onNodeWithTag(CLEAR_BUTTON_TEST_TAG).performClick()
        composeTestRule.onNode(hasText(initialText)).assertDoesNotExist()
    }

    @Test
    fun `텍스트를 입력할 수 있다`() {
        val inputText = "새로운 제목"

        composeTestRule.setContent {
            DiaryTheme {
                TitleCard()
            }
        }

        composeTestRule.onNode(hasText("제목")).performTextInput(inputText)
        composeTestRule.onNodeWithText(inputText).assertIsDisplayed()
    }

    @Test
    fun `configuration change 후 상태가 유지된다`() {
        val inputText = "새로 입력한 제목"
        lateinit var state: TitleCardState

        val restorationTester = StateRestorationTester(composeTestRule)
        restorationTester.setContent {
            DiaryTheme {
                val state = rememberTitleCardState()
                    .also { state = it }

                TitleCard(state = state)
            }
        }

        composeTestRule.onNode(hasText("제목")).performTextInput(inputText)

        restorationTester.emulateSavedInstanceStateRestore()

        composeTestRule.onNodeWithText(inputText).assertIsDisplayed()
        assertEquals(inputText, state.textFieldState.text.toString())
    }
}
