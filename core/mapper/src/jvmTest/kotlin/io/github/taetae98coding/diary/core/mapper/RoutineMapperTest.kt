package io.github.taetae98coding.diary.core.mapper

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.github.taetae98coding.diary.core.database.api.entity.RRuleDiaryByDayLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.RoutineLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.RoutineRRuleLocalEntity
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.DayOfWeek

class RoutineMapperTest : FunSpec() {
    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        test("Local to Domain — RRule 리스트 매핑") {
            val rRules = listOf(
                RoutineRRuleLocalEntity(
                    diaryByDay = RRuleDiaryByDayLocalEntity(days = listOf(DayOfWeek.MONDAY)),
                    byMonthDay = emptyList(),
                ),
                RoutineRRuleLocalEntity(
                    diaryByDay = RRuleDiaryByDayLocalEntity(),
                    byMonthDay = listOf(15),
                ),
            )
            val entity = fixtureMonkey.giveMeKotlinBuilder<RoutineLocalEntity>()
                .setExp(RoutineLocalEntity::rRules, rRules)
                .sample()

            val result = entity.toDomain()

            result.id shouldBe entity.id
            result.rRules.size shouldBe 2
            result.rDates shouldBe entity.rDates.toSet()
            result.exDates shouldBe entity.exDates.toSet()
            result.isFinished shouldBe entity.isFinished
            result.isDeleted shouldBe entity.isDeleted
        }

        test("Local to Domain — 빈 RRule 리스트") {
            val entity = fixtureMonkey.giveMeKotlinBuilder<RoutineLocalEntity>()
                .setExp(RoutineLocalEntity::rRules, emptyList<RoutineRRuleLocalEntity>())
                .sample()

            val result = entity.toDomain()

            result.rRules shouldBe emptyList()
        }
    }
}
