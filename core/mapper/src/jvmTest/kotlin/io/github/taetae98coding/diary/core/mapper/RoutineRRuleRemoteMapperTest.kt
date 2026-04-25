package io.github.taetae98coding.diary.core.mapper

import io.github.taetae98coding.diary.core.database.api.entity.RRuleDiaryByDayLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.RoutineRRuleLocalEntity
import io.github.taetae98coding.diary.core.network.api.entity.RRuleDiaryByDayRemoteEntity
import io.github.taetae98coding.diary.core.network.api.entity.RoutineRRuleRemoteEntity
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.DayOfWeek

class RoutineRRuleRemoteMapperTest : FunSpec() {
    init {
        test("Local to Remote") {
            val entity = RoutineRRuleLocalEntity(
                diaryByDay = RRuleDiaryByDayLocalEntity(
                    days = listOf(DayOfWeek.MONDAY),
                    ordinal = 1,
                ),
                byMonthDay = listOf(15, -1),
            )

            val result = entity.toRemote()

            result.diaryByDay shouldBe entity.diaryByDay.toRemote()
            result.byMonthDay shouldBe listOf(15, -1)
        }

        test("Local to Remote — default") {
            val result = RoutineRRuleLocalEntity().toRemote()

            result.diaryByDay shouldBe RRuleDiaryByDayRemoteEntity()
            result.byMonthDay shouldBe emptyList()
        }

        test("Remote to Local") {
            val entity = RoutineRRuleRemoteEntity(
                diaryByDay = RRuleDiaryByDayRemoteEntity(
                    days = listOf(DayOfWeek.SUNDAY),
                    ordinal = -1,
                ),
                byMonthDay = listOf(1),
            )

            val result = entity.toLocal()

            result.diaryByDay shouldBe entity.diaryByDay.toLocal()
            result.byMonthDay shouldBe listOf(1)
        }

        test("Remote to Local — default") {
            val result = RoutineRRuleRemoteEntity().toLocal()

            result.diaryByDay shouldBe RRuleDiaryByDayLocalEntity()
            result.byMonthDay shouldBe emptyList()
        }
    }
}
