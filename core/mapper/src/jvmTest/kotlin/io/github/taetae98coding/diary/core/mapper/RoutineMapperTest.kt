package io.github.taetae98coding.diary.core.mapper

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.github.taetae98coding.diary.core.database.api.entity.RoutineDetailLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.RoutineLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.RoutineRRuleLocalEntity
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDate

class RoutineMapperTest : FunSpec() {
    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        test("Local to Domain") {
            val entity = fixtureMonkey.giveMeKotlinBuilder<RoutineLocalEntity>()
                .set("detail.start", LocalDate(2026, 1, 1))
                .set("detail.endInclusive", LocalDate(2026, 12, 31))
                .set(
                    RoutineLocalEntity::rRules,
                    listOf(
                        RoutineRRuleLocalEntity.ByDay(dayOfWeek = 1),
                        RoutineRRuleLocalEntity.ByMonthDay(day = 15),
                    ),
                )
                .set(RoutineLocalEntity::rDates, listOf(LocalDate(2026, 5, 1)))
                .set(RoutineLocalEntity::exDates, listOf(LocalDate(2026, 4, 13)))
                .sample()

            val result = entity.toDomain()

            result.id shouldBe entity.id
            result.detail shouldBe entity.detail.toDomain()
            result.rRules shouldBe entity.rRules.map { it.toDomain() }
            result.rDates shouldBe entity.rDates.toSet()
            result.exDates shouldBe entity.exDates.toSet()
            result.isFinished shouldBe entity.isFinished
            result.isDeleted shouldBe entity.isDeleted
            result.updatedAt shouldBe entity.updatedAt
            result.createdAt shouldBe entity.createdAt
        }

        test("Local to Domain - rdates/exdates 중복 제거") {
            val duplicateDate = LocalDate(2026, 5, 1)
            val entity = fixtureMonkey.giveMeKotlinBuilder<RoutineLocalEntity>()
                .set("detail.start", LocalDate(2026, 1, 1))
                .set("detail.endInclusive", LocalDate(2026, 12, 31))
                .set(RoutineLocalEntity::rRules, emptyList<RoutineRRuleLocalEntity>())
                .set(RoutineLocalEntity::rDates, listOf(duplicateDate, duplicateDate))
                .set(RoutineLocalEntity::exDates, listOf(duplicateDate, duplicateDate))
                .sample()

            val result = entity.toDomain()

            result.rDates.size shouldBe 1
            result.exDates.size shouldBe 1
        }
    }
}
