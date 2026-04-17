package io.github.taetae98coding.diary.core.mapper

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.github.taetae98coding.diary.core.database.api.entity.RoutineDetailLocalEntity
import io.github.taetae98coding.diary.core.model.routine.RoutineDetail
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class RoutineDetailMapperTest : FunSpec() {
    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        test("Domain to Local") {
            val detail = fixtureMonkey.giveMeKotlinBuilder<RoutineDetail>().sample()

            val result = detail.toLocal()

            result.title shouldBe detail.title
            result.description shouldBe detail.description
            result.start shouldBe detail.start
            result.endInclusive shouldBe detail.endInclusive
            result.color shouldBe detail.color
            result.routineCount shouldBe detail.routineCount
        }

        test("Local to Domain") {
            val entity = fixtureMonkey.giveMeKotlinBuilder<RoutineDetailLocalEntity>().sample()

            val result = entity.toDomain()

            result.title shouldBe entity.title
            result.description shouldBe entity.description
            result.start shouldBe entity.start
            result.endInclusive shouldBe entity.endInclusive
            result.color shouldBe entity.color
            result.routineCount shouldBe entity.routineCount
        }
    }
}
