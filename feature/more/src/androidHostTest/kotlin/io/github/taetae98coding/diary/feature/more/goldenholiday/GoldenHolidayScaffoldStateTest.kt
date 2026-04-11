package io.github.taetae98coding.diary.feature.more.goldenholiday

import io.kotest.matchers.shouldBe
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class GoldenHolidayScaffoldStateTest {
    private lateinit var state: GoldenHolidayScaffoldState

    @Before
    fun setup() {
        state = GoldenHolidayScaffoldState(
            dialogState = io.github.taetae98coding.diary.compose.core.dialog.DialogState(),
            pagerState = androidx.compose.foundation.pager.PagerState(currentPage = 0, pageCount = { Int.MAX_VALUE }),
        )
    }

    @Test
    fun `annualLeave 초기값은 0이다`() {
        state.annualLeave shouldBe 0
    }

    @Test
    fun `plusAnnualLeave를 호출하면 1 증가한다`() {
        state.plusAnnualLeave()
        state.annualLeave shouldBe 1
    }

    @Test
    fun `minusAnnualLeave를 호출하면 1 감소한다`() {
        state.plusAnnualLeave()
        state.plusAnnualLeave()
        state.minusAnnualLeave()
        state.annualLeave shouldBe 1
    }

    @Test
    fun `annualLeave는 0 미만으로 내려가지 않는다`() {
        state.minusAnnualLeave()
        state.annualLeave shouldBe 0
    }

    @Test
    fun `sortOrder 초기값은 LONGEST_FIRST이다`() {
        state.sortOrder shouldBe GoldenHolidaySortOrder.LONGEST_FIRST
    }

    @Test
    fun `updateSortOrder로 정렬 순서를 변경할 수 있다`() {
        state.updateSortOrder(GoldenHolidaySortOrder.START_DATE)
        state.sortOrder shouldBe GoldenHolidaySortOrder.START_DATE
    }
}
