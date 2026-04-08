package io.github.taetae98coding.diary.core.model.routine

import io.github.taetae98coding.diary.library.datetime.toSundayBasedNumber
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.yearMonth

public sealed class RoutineRRule {
    public abstract fun isActive(localDate: LocalDate): Boolean

    public data class ByDay(
        val dayOfWeek: DayOfWeek,
        val ordinal: Int? = null,
    ) : RoutineRRule() {
        override fun isActive(localDate: LocalDate): Boolean {
            if (localDate.dayOfWeek != dayOfWeek) return false
            if (ordinal == null) return true

            val weekEpochDays = localDate.minus(localDate.dayOfWeek.toSundayBasedNumber(), DateTimeUnit.DAY).toEpochDays()

            return if (ordinal > 0) {
                val firstWeekEpochDays = localDate.yearMonth.firstDay.minus(localDate.yearMonth.firstDay.dayOfWeek.toSundayBasedNumber(), DateTimeUnit.DAY).toEpochDays()

                (weekEpochDays - firstWeekEpochDays).toInt() / 7 == ordinal - 1
            } else {
                val lastWeekEpochDays = localDate.yearMonth.lastDay.minus(localDate.yearMonth.lastDay.dayOfWeek.toSundayBasedNumber(), DateTimeUnit.DAY).toEpochDays()

                (weekEpochDays - lastWeekEpochDays).toInt() / 7 == ordinal + 1
            }
        }
    }

    public data class ByMonthDay(val day: Int) : RoutineRRule() {
        override fun isActive(localDate: LocalDate): Boolean {
            return localDate.day == day
        }
    }
}
