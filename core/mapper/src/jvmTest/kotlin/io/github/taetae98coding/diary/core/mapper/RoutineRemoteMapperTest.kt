package io.github.taetae98coding.diary.core.mapper

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.github.taetae98coding.diary.core.database.api.entity.RRuleDiaryByDayLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.RoutineLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.RoutineRRuleLocalEntity
import io.github.taetae98coding.diary.core.network.api.entity.RRuleDiaryByDayRemoteEntity
import io.github.taetae98coding.diary.core.network.api.entity.RoutineRRuleRemoteEntity
import io.github.taetae98coding.diary.core.network.api.entity.RoutineRemoteEntity
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.DayOfWeek

class RoutineRemoteMapperTest : FunSpec() {
    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        test("Local to Remote") {
            val rRules = listOf(
                RoutineRRuleLocalEntity(
                    diaryByDay = RRuleDiaryByDayLocalEntity(days = listOf(DayOfWeek.MONDAY)),
                ),
                RoutineRRuleLocalEntity(byMonthDay = listOf(15)),
            )
            val entity = fixtureMonkey.giveMeKotlinBuilder<RoutineLocalEntity>()
                .setExp(RoutineLocalEntity::rRules, rRules)
                .sample()

            val result = entity.toRemote()

            result.id shouldBe entity.id
            result.detail shouldBe entity.detail.toRemote()
            result.rRules shouldBe rRules.map { it.toRemote() }
            result.isFinished shouldBe entity.isFinished
            result.isDeleted shouldBe entity.isDeleted
            result.updatedAt shouldBe entity.updatedAt
            result.createdAt shouldBe entity.createdAt
        }

        test("Remote to Local") {
            val rRules = listOf(
                RoutineRRuleRemoteEntity(
                    diaryByDay = RRuleDiaryByDayRemoteEntity(days = listOf(DayOfWeek.SATURDAY)),
                ),
            )
            val entity = fixtureMonkey.giveMeKotlinBuilder<RoutineRemoteEntity>()
                .setExp(RoutineRemoteEntity::rRules, rRules)
                .sample()

            val result = entity.toLocal()

            result.id shouldBe entity.id
            result.detail shouldBe entity.detail.toLocal()
            result.rRules shouldBe rRules.map { it.toLocal() }
            result.isFinished shouldBe entity.isFinished
            result.isDeleted shouldBe entity.isDeleted
            result.updatedAt shouldBe entity.updatedAt
            result.createdAt shouldBe entity.createdAt
        }
    }
}
