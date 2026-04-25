package io.github.taetae98coding.diary.core.mapper

import io.github.taetae98coding.diary.core.database.api.entity.RRuleDiaryByDayLocalEntity
import io.github.taetae98coding.diary.core.model.routine.RRuleDiaryByDay
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.DayOfWeek

class RRuleDiaryByDayMapperTest : FunSpec() {
    init {
        test("Domain to Local") {
            val diaryByDay = RRuleDiaryByDay(
                days = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
                ordinal = 2,
            )

            val result = diaryByDay.toLocal()

            result.days.toSet() shouldBe setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)
            result.ordinal shouldBe 2
        }

        test("Domain to Local — empty") {
            val diaryByDay = RRuleDiaryByDay()

            val result = diaryByDay.toLocal()

            result.days shouldBe emptyList()
            result.ordinal shouldBe null
        }

        test("Local to Domain") {
            val entity = RRuleDiaryByDayLocalEntity(
                days = listOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY),
                ordinal = 3,
            )

            val result = entity.toDomain()

            result.days shouldBe setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)
            result.ordinal shouldBe 3
        }

        test("Local to Domain — default") {
            val entity = RRuleDiaryByDayLocalEntity()

            val result = entity.toDomain()

            result.days shouldBe emptySet()
            result.ordinal shouldBe null
        }
    }
}
