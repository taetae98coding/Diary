package io.github.taetae98coding.diary.core.mapper

import io.github.taetae98coding.diary.core.database.api.entity.RRuleDiaryByDayLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.RoutineRRuleLocalEntity
import io.github.taetae98coding.diary.core.model.routine.RRuleDiaryByDay
import io.github.taetae98coding.diary.core.model.routine.RoutineRRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.DayOfWeek

class RoutineRRuleMapperTest : FunSpec() {
    init {
        test("Domain to Local") {
            val rRule = RoutineRRule(
                diaryByDay = RRuleDiaryByDay(days = setOf(DayOfWeek.MONDAY), ordinal = 2),
                byMonthDay = setOf(15, -1),
            )

            val result = rRule.toLocal()

            result.diaryByDay.days shouldBe listOf(DayOfWeek.MONDAY)
            result.diaryByDay.ordinal shouldBe 2
            result.byMonthDay.toSet() shouldBe setOf(15, -1)
        }

        test("Domain to Local — empty fields") {
            val rRule = RoutineRRule()

            val result = rRule.toLocal()

            result.diaryByDay shouldBe RRuleDiaryByDayLocalEntity()
            result.byMonthDay shouldBe emptyList()
        }

        test("Local to Domain") {
            val entity = RoutineRRuleLocalEntity(
                diaryByDay = RRuleDiaryByDayLocalEntity(days = listOf(DayOfWeek.TUESDAY), ordinal = 3),
                byMonthDay = listOf(1, 15),
            )

            val result = entity.toDomain()

            result.diaryByDay.days shouldBe setOf(DayOfWeek.TUESDAY)
            result.diaryByDay.ordinal shouldBe 3
            result.byMonthDay shouldBe setOf(1, 15)
        }

        test("Local to Domain — default") {
            val entity = RoutineRRuleLocalEntity()

            val result = entity.toDomain()

            result.diaryByDay shouldBe RRuleDiaryByDay()
            result.byMonthDay shouldBe emptySet<Int>()
        }
    }
}
