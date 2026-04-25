package io.github.taetae98coding.diary.core.mapper

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.github.taetae98coding.diary.core.database.api.entity.RoutineDetailLocalEntity
import io.github.taetae98coding.diary.core.network.api.entity.RoutineDetailRemoteEntity
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDate

class RoutineDetailRemoteMapperTest : FunSpec() {
    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        test("Local to Remote") {
            val entity = fixtureMonkey.giveMeKotlinBuilder<RoutineDetailLocalEntity>()
                .set(RoutineDetailLocalEntity::start, LocalDate(2026, 1, 1))
                .set(RoutineDetailLocalEntity::endInclusive, LocalDate(2026, 1, 31))
                .sample()

            val result = entity.toRemote()

            result.title shouldBe entity.title
            result.description shouldBe entity.description
            result.start shouldBe entity.start
            result.endInclusive shouldBe entity.endInclusive
            result.color shouldBe entity.color
            result.routineCount shouldBe entity.routineCount
        }

        test("Local to Remote — start가 null") {
            val entity = fixtureMonkey.giveMeKotlinBuilder<RoutineDetailLocalEntity>()
                .setNull(RoutineDetailLocalEntity::start)
                .setNull(RoutineDetailLocalEntity::endInclusive)
                .sample()

            val result = entity.toRemote()

            result.start shouldBe null
            result.endInclusive shouldBe null
        }

        test("Remote to Local") {
            val entity = fixtureMonkey.giveMeKotlinBuilder<RoutineDetailRemoteEntity>()
                .set(RoutineDetailRemoteEntity::start, LocalDate(2026, 1, 1))
                .set(RoutineDetailRemoteEntity::endInclusive, LocalDate(2026, 1, 31))
                .sample()

            val result = entity.toLocal()

            result.title shouldBe entity.title
            result.description shouldBe entity.description
            result.start shouldBe entity.start
            result.endInclusive shouldBe entity.endInclusive
            result.color shouldBe entity.color
            result.routineCount shouldBe entity.routineCount
        }

        test("Remote to Local — start가 null") {
            val entity = fixtureMonkey.giveMeKotlinBuilder<RoutineDetailRemoteEntity>()
                .setNull(RoutineDetailRemoteEntity::start)
                .setNull(RoutineDetailRemoteEntity::endInclusive)
                .sample()

            val result = entity.toLocal()

            result.start shouldBe null
            result.endInclusive shouldBe null
        }
    }
}
