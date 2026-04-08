package io.github.taetae98coding.diary.core.mapper

import io.github.taetae98coding.diary.core.database.api.entity.RoutineRRuleLocalEntity
import io.github.taetae98coding.diary.core.model.routine.RoutineRRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.DayOfWeek

class RoutineRRuleMapperTest : FunSpec() {
    init {
        test("Domain to Local - ByDay (매주)") {
            val rRule = RoutineRRule.ByDay(dayOfWeek = DayOfWeek.MONDAY)

            val result = rRule.toLocal()

            result shouldBe RoutineRRuleLocalEntity.ByDay(dayOfWeek = 1)
        }

        test("Domain to Local - ByDay (n번째주)") {
            val rRule = RoutineRRule.ByDay(dayOfWeek = DayOfWeek.FRIDAY, ordinal = 1)

            val result = rRule.toLocal()

            result shouldBe RoutineRRuleLocalEntity.ByDay(dayOfWeek = 5, ordinal = 1)
        }

        test("Domain to Local - ByMonthDay") {
            val rRule = RoutineRRule.ByMonthDay(day = 15)

            val result = rRule.toLocal()

            result shouldBe RoutineRRuleLocalEntity.ByMonthDay(day = 15)
        }

        test("Local to Domain - ByDay (매주)") {
            val entity = RoutineRRuleLocalEntity.ByDay(dayOfWeek = 1)

            val result = entity.toDomain()

            result shouldBe RoutineRRule.ByDay(dayOfWeek = DayOfWeek.MONDAY)
        }

        test("Local to Domain - ByDay (n번째주)") {
            val entity = RoutineRRuleLocalEntity.ByDay(dayOfWeek = 5, ordinal = -1)

            val result = entity.toDomain()

            result shouldBe RoutineRRule.ByDay(dayOfWeek = DayOfWeek.FRIDAY, ordinal = -1)
        }

        test("Local to Domain - ByMonthDay") {
            val entity = RoutineRRuleLocalEntity.ByMonthDay(day = 15)

            val result = entity.toDomain()

            result shouldBe RoutineRRule.ByMonthDay(day = 15)
        }
    }
}
