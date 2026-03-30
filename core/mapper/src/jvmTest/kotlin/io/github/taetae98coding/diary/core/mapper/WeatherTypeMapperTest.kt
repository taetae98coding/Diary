package io.github.taetae98coding.diary.core.mapper

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.github.taetae98coding.diary.core.weather.network.api.entity.WeatherTypeRemoteEntity
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class WeatherTypeMapperTest : FunSpec() {
    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        test("Remote to Domain") {
            val entity = fixtureMonkey.giveMeKotlinBuilder<WeatherTypeRemoteEntity>()
                .sample()

            val result = entity.toDomain()

            result.icon shouldBe entity.icon.image
            result.description shouldBe entity.description
        }
    }
}
