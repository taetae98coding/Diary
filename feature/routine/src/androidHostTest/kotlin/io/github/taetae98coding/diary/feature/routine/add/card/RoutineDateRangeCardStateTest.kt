package io.github.taetae98coding.diary.feature.routine.add.card

import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDate
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RoutineDateRangeCardStateTest {
    @Test
    fun `초기값이 null이면 start와 endInclusive 모두 null이다`() {
        val state = RoutineDateRangeCardState(initialStart = null, initialEndInclusive = null)
        state.start.shouldBeNull()
        state.endInclusive.shouldBeNull()
    }

    @Test
    fun `초기값이 있으면 해당 값으로 설정된다`() {
        val start = LocalDate(2026, 1, 1)
        val end = LocalDate(2026, 1, 31)
        val state = RoutineDateRangeCardState(initialStart = start, initialEndInclusive = end)
        state.start shouldBe start
        state.endInclusive shouldBe end
    }

    @Test
    fun `updateStart - start가 endInclusive보다 크면 endInclusive도 start로 변경된다`() {
        val state = RoutineDateRangeCardState(
            initialStart = LocalDate(2026, 1, 1),
            initialEndInclusive = LocalDate(2026, 1, 15),
        )
        state.updateStart(LocalDate(2026, 1, 20))
        state.start shouldBe LocalDate(2026, 1, 20)
        state.endInclusive shouldBe LocalDate(2026, 1, 20)
    }

    @Test
    fun `updateStart - start가 endInclusive보다 작으면 endInclusive는 변경되지 않는다`() {
        val state = RoutineDateRangeCardState(
            initialStart = LocalDate(2026, 1, 1),
            initialEndInclusive = LocalDate(2026, 1, 15),
        )
        state.updateStart(LocalDate(2026, 1, 10))
        state.start shouldBe LocalDate(2026, 1, 10)
        state.endInclusive shouldBe LocalDate(2026, 1, 15)
    }

    @Test
    fun `updateEndInclusive - endInclusive가 start보다 작으면 start도 endInclusive로 변경된다`() {
        val state = RoutineDateRangeCardState(
            initialStart = LocalDate(2026, 1, 15),
            initialEndInclusive = LocalDate(2026, 1, 31),
        )
        state.updateEndInclusive(LocalDate(2026, 1, 10))
        state.endInclusive shouldBe LocalDate(2026, 1, 10)
        state.start shouldBe LocalDate(2026, 1, 10)
    }

    @Test
    fun `updateEndInclusive - endInclusive가 start보다 크면 start는 변경되지 않는다`() {
        val state = RoutineDateRangeCardState(
            initialStart = LocalDate(2026, 1, 15),
            initialEndInclusive = LocalDate(2026, 1, 31),
        )
        state.updateEndInclusive(LocalDate(2026, 2, 15))
        state.endInclusive shouldBe LocalDate(2026, 2, 15)
        state.start shouldBe LocalDate(2026, 1, 15)
    }

    @Test
    fun `updateStart - endInclusive가 null이면 swap하지 않는다`() {
        val state = RoutineDateRangeCardState(initialStart = null, initialEndInclusive = null)
        state.updateStart(LocalDate(2026, 3, 1))
        state.start shouldBe LocalDate(2026, 3, 1)
        state.endInclusive.shouldBeNull()
    }

    @Test
    fun `updateEndInclusive - start가 null이면 swap하지 않는다`() {
        val state = RoutineDateRangeCardState(initialStart = null, initialEndInclusive = null)
        state.updateEndInclusive(LocalDate(2026, 3, 1))
        state.endInclusive shouldBe LocalDate(2026, 3, 1)
        state.start.shouldBeNull()
    }

    @Test
    fun `clear를 호출하면 start와 endInclusive 모두 null이 된다`() {
        val state = RoutineDateRangeCardState(
            initialStart = LocalDate(2026, 1, 1),
            initialEndInclusive = LocalDate(2026, 1, 31),
        )
        state.clear()
        state.start.shouldBeNull()
        state.endInclusive.shouldBeNull()
    }
}
