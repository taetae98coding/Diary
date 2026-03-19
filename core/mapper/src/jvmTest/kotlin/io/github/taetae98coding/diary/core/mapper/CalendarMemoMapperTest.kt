package io.github.taetae98coding.diary.core.mapper

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.github.taetae98coding.diary.core.database.api.entity.CalendarMemoLocalEntity
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDateTime

class CalendarMemoMapperTest : FunSpec() {
    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        test("Local to Domain") {
            val entity = fixtureMonkey.giveMeKotlinBuilder<CalendarMemoLocalEntity>()
                .set(CalendarMemoLocalEntity::start, LocalDateTime(2026, 1, 1, 0, 0))
                .set(CalendarMemoLocalEntity::endInclusive, LocalDateTime(2026, 1, 2, 0, 0))
                .sample()

            val result = entity.toDomain()

            result.id shouldBe entity.id
            result.title shouldBe entity.title
            result.isAllDay shouldBe entity.isAllDay
            result.localDateTimeRange.start shouldBe entity.start
            result.localDateTimeRange.endInclusive shouldBe entity.endInclusive
            result.color shouldBe entity.color
        }
    }
}
