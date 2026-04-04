package io.github.taetae98coding.diary.core.mapper

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.github.taetae98coding.diary.core.holiday.database.api.entity.HolidayLocalEntity
import io.github.taetae98coding.diary.core.holiday.network.api.entity.HolidayRemoteEntity
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.http.encodeURLParameter
import kotlinx.datetime.LocalDate

class HolidayMapperTest : FunSpec() {
    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        test("Local to Domain") {
            val entity = fixtureMonkey.giveMeKotlinBuilder<HolidayLocalEntity>()
                .set("start", LocalDate(2026, 1, 1))
                .set("endInclusive", LocalDate(2026, 1, 2))
                .sample()

            val result = entity.toDomain()

            result.name shouldBe entity.name
            result.isHoliday shouldBe entity.isHoliday
            result.localDateRange shouldBe entity.start..entity.endInclusive
            result.link shouldBe "https://namu.wiki/w/${entity.name.encodeURLParameter()}"
        }

        test("Remote to Local") {
            val entity = fixtureMonkey.giveMeKotlinBuilder<HolidayRemoteEntity>()
                .set("start", LocalDate(2026, 1, 1))
                .set("endInclusive", LocalDate(2026, 1, 2))
                .sample()

            val result = entity.toLocal()

            result.name shouldBe entity.name
            result.isHoliday shouldBe entity.isHoliday
            result.start shouldBe entity.start
            result.endInclusive shouldBe entity.endInclusive
        }
    }
}
