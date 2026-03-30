package io.github.taetae98coding.diary.core.mapper

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.github.taetae98coding.diary.core.weather.network.api.entity.WeahterMainRemoteEntity
import io.github.taetae98coding.diary.core.weather.network.api.entity.WeatherRemoteEntity
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class WeatherMapperTest : FunSpec() {
    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        test("Remote to Domain") {
            val entity = fixtureMonkey.giveMeKotlinBuilder<WeatherRemoteEntity>()
                .sample()

            val result = entity.toDomain()

            result.typeList.size shouldBe entity.weather.size
            result.typeList.forEachIndexed { index, type ->
                type.icon shouldBe entity.weather[index].icon.image
                type.description shouldBe entity.weather[index].description
            }
            result.temperature shouldBe entity.main.temperature
            result.minTemperature shouldBe entity.main.minTemperature
            result.maxTemperature shouldBe entity.main.maxTemperature
            result.instant shouldBe entity.instant
        }

        test("Remote to Domain with null temperatures") {
            val main = fixtureMonkey.giveMeKotlinBuilder<WeahterMainRemoteEntity>()
                .setNull(WeahterMainRemoteEntity::minTemperature)
                .setNull(WeahterMainRemoteEntity::maxTemperature)
                .sample()

            val entity = fixtureMonkey.giveMeKotlinBuilder<WeatherRemoteEntity>()
                .set(WeatherRemoteEntity::main, main)
                .sample()

            val result = entity.toDomain()

            result.minTemperature shouldBe null
            result.maxTemperature shouldBe null
        }
    }
}
