package io.github.taetae98coding.diary.core.mapper

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.github.taetae98coding.diary.core.database.api.entity.MemoDetailLocalEntity
import io.github.taetae98coding.diary.core.network.api.entity.MemoDetailRemoteEntity
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDateTime

class MemoDetailRemoteMapperTest : FunSpec() {
    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        test("Local to Remote") {
            val entity = fixtureMonkey.giveMeKotlinBuilder<MemoDetailLocalEntity>()
                .set(MemoDetailLocalEntity::start, LocalDateTime(2026, 1, 1, 0, 0))
                .set(MemoDetailLocalEntity::endInclusive, LocalDateTime(2026, 1, 2, 0, 0))
                .sample()

            val result = entity.toRemote()

            result.title shouldBe entity.title
            result.description shouldBe entity.description
            result.isAllDay shouldBe entity.isAllDay
            result.start shouldBe entity.start
            result.endInclusive shouldBe entity.endInclusive
            result.color shouldBe entity.color
        }

        test("Local to Remote - start가 null") {
            val entity = fixtureMonkey.giveMeKotlinBuilder<MemoDetailLocalEntity>()
                .setNull(MemoDetailLocalEntity::start)
                .set(MemoDetailLocalEntity::endInclusive, LocalDateTime(2026, 1, 2, 0, 0))
                .sample()

            val result = entity.toRemote()

            result.start shouldBe null
            result.endInclusive shouldBe entity.endInclusive
        }

        test("Remote to Local") {
            val entity = fixtureMonkey.giveMeKotlinBuilder<MemoDetailRemoteEntity>()
                .set(MemoDetailRemoteEntity::start, LocalDateTime(2026, 1, 1, 0, 0))
                .set(MemoDetailRemoteEntity::endInclusive, LocalDateTime(2026, 1, 2, 0, 0))
                .sample()

            val result = entity.toLocal()

            result.title shouldBe entity.title
            result.description shouldBe entity.description
            result.isAllDay shouldBe entity.isAllDay
            result.start shouldBe entity.start
            result.endInclusive shouldBe entity.endInclusive
            result.color shouldBe entity.color
        }

        test("Remote to Local - start가 null") {
            val entity = fixtureMonkey.giveMeKotlinBuilder<MemoDetailRemoteEntity>()
                .setNull(MemoDetailRemoteEntity::start)
                .set(MemoDetailRemoteEntity::endInclusive, LocalDateTime(2026, 1, 2, 0, 0))
                .sample()

            val result = entity.toLocal()

            result.start shouldBe null
            result.endInclusive shouldBe entity.endInclusive
        }
    }
}
