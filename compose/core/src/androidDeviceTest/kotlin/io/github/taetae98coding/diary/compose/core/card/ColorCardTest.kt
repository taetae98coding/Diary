package io.github.taetae98coding.diary.compose.core.card

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.performClick
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ColorCardTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `Hex 색상 코드가 표시된다`() {
        composeTestRule.setContent {
            DiaryTheme {
                ColorCard(state = rememberColorCardState(initialColor = Color.Red))
            }
        }

        composeTestRule.onNode(hasText("#FF0000")).assertIsDisplayed()
    }

    @Test
    fun `RGB 텍스트가 표시된다`() {
        composeTestRule.setContent {
            DiaryTheme {
                ColorCard(state = rememberColorCardState(initialColor = Color.Red))
            }
        }

        composeTestRule.onNode(hasText("RGB(255, 0, 0)")).assertIsDisplayed()
    }

    @Test
    fun `클릭하면 ColorPicker 다이얼로그가 표시된다`() {
        composeTestRule.setContent {
            DiaryTheme {
                ColorCard(state = rememberColorCardState(initialColor = Color.Red))
            }
        }

        composeTestRule.onNode(hasText("#FF0000")).performClick()
        composeTestRule.onNode(hasText("확인")).assertIsDisplayed()
    }

    @Test
    fun `configuration change 후 색상 상태가 유지된다`() {
        val initialColor = Color.Blue
        lateinit var state: ColorCardState

        val restorationTester = StateRestorationTester(composeTestRule)
        restorationTester.setContent {
            DiaryTheme {
                val state = rememberColorCardState(initialColor = initialColor)
                    .also { state = it }

                ColorCard(state = state)
            }
        }

        restorationTester.emulateSavedInstanceStateRestore()

        assertEquals(initialColor.red, state.value.red, 0.01f)
        assertEquals(initialColor.green, state.value.green, 0.01f)
        assertEquals(initialColor.blue, state.value.blue, 0.01f)
    }
}
