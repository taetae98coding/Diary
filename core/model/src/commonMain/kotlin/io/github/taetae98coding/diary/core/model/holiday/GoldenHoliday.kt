package io.github.taetae98coding.diary.core.model.holiday

import kotlinx.datetime.LocalDateRange
import kotlinx.datetime.YearMonth
import kotlinx.datetime.yearMonth

public data class GoldenHoliday(
    val localDateRange: LocalDateRange,
    val holiday: List<Holiday>,
    val annualLeave: List<LocalDateRange>,
) {
    val totalDays: Int
        get() = localDateRange.size

    val yearMonth: YearMonth
        get() {
            return localDateRange.groupBy { it.yearMonth }
                .maxBy { it.value.size }
                .key
        }
}
