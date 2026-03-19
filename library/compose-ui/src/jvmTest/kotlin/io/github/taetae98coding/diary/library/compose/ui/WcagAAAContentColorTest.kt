package io.github.taetae98coding.diary.library.compose.ui

import androidx.compose.ui.graphics.Color
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class WcagAAAContentColorTest : FunSpec() {
    init {
        test("검정 배경에서 흰색 반환") {
            Color.Black.wcagAAAContentColor() shouldBe Color.White
        }

        test("흰색 배경에서 검정 반환") {
            Color.White.wcagAAAContentColor() shouldBe Color.Black
        }

        test("어두운 색 배경에서 흰색 반환") {
            Color(0xFF1A1A1A).wcagAAAContentColor() shouldBe Color.White
        }

        test("밝은 색 배경에서 검정 반환") {
            Color(0xFFE0E0E0).wcagAAAContentColor() shouldBe Color.Black
        }

        test("빨간색 배경에서 대비가 높은 색 반환") {
            Color.Red.wcagAAAContentColor() shouldBe Color.Black
        }

        test("노란색 배경에서 검정 반환") {
            Color.Yellow.wcagAAAContentColor() shouldBe Color.Black
        }

        test("파란색 배경에서 흰색 반환") {
            Color.Blue.wcagAAAContentColor() shouldBe Color.White
        }

        test("커스텀 팔레트에서 대비가 가장 높은 색 반환") {
            val palette = listOf(Color.Red, Color.Green, Color.Blue)
            val result = Color.Black.wcagAAAContentColor(palette)

            result shouldBe Color.Green
        }

        test("빈 팔레트에서 White 반환") {
            Color.Black.wcagAAAContentColor(emptyList()) shouldBe Color.White
        }
    }
}
