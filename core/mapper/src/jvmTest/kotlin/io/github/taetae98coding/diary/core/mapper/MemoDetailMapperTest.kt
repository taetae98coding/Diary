package io.github.taetae98coding.diary.core.mapper

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.github.taetae98coding.diary.core.database.api.entity.MemoDetailLocalEntity
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDateTime

class MemoDetailMapperTest : FunSpec() {
    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        test("Domain to Local") {
            val detail = fixtureMonkey.giveMeKotlinBuilder<MemoDetail>()
                .set(MemoDetail::localDateTimeRange, LocalDateTime(2026, 1, 1, 0, 0)..LocalDateTime(2026, 1, 2, 0, 0))
                .sample()

            val result = detail.toLocal()

            result.title shouldBe detail.title
            result.description shouldBe detail.description
            result.isAllDay shouldBe detail.isAllDay
            result.start shouldBe detail.localDateTimeRange?.start
            result.endInclusive shouldBe detail.localDateTimeRange?.endInclusive
            result.color shouldBe detail.color
        }

        test("Domain to Local - localDateTimeRange가 null") {
            val detail = fixtureMonkey.giveMeKotlinBuilder<MemoDetail>()
                .setNull(MemoDetail::localDateTimeRange)
                .sample()

            val result = detail.toLocal()

            result.start shouldBe null
            result.endInclusive shouldBe null
        }

        test("Local to Domain") {
            val entity = fixtureMonkey.giveMeKotlinBuilder<MemoDetailLocalEntity>()
                .set(MemoDetailLocalEntity::start, LocalDateTime(2026, 1, 1, 0, 0))
                .set(MemoDetailLocalEntity::endInclusive, LocalDateTime(2026, 1, 2, 0, 0))
                .sample()

            val result = entity.toDomain()

            result.title shouldBe entity.title
            result.description shouldBe entity.description
            result.isAllDay shouldBe entity.isAllDay
            result.localDateTimeRange?.start shouldBe entity.start
            result.localDateTimeRange?.endInclusive shouldBe entity.endInclusive
            result.color shouldBe entity.color
        }

        test("Local to Domain - start가 null") {
            val entity = fixtureMonkey.giveMeKotlinBuilder<MemoDetailLocalEntity>()
                .setNull(MemoDetailLocalEntity::start)
                .set(MemoDetailLocalEntity::endInclusive, LocalDateTime(2026, 1, 2, 0, 0))
                .sample()

            val result = entity.toDomain()

            result.localDateTimeRange shouldBe null
        }

        test("Local to Domain - endInclusive가 null") {
            val entity = fixtureMonkey.giveMeKotlinBuilder<MemoDetailLocalEntity>()
                .set(MemoDetailLocalEntity::start, LocalDateTime(2026, 1, 1, 0, 0))
                .setNull(MemoDetailLocalEntity::endInclusive)
                .sample()

            val result = entity.toDomain()

            result.localDateTimeRange shouldBe null
        }

        test("Local to Domain - start와 endInclusive 모두 null") {
            val entity = fixtureMonkey.giveMeKotlinBuilder<MemoDetailLocalEntity>()
                .setNull(MemoDetailLocalEntity::start)
                .setNull(MemoDetailLocalEntity::endInclusive)
                .sample()

            val result = entity.toDomain()

            result.localDateTimeRange shouldBe null
        }
    }
}
