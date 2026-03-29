package io.github.taetae98coding.diary.core.mapper

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.github.taetae98coding.diary.core.database.api.entity.TagDetailLocalEntity
import io.github.taetae98coding.diary.core.model.tag.TagDetail
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class TagDetailMapperTest : FunSpec() {
    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        test("Domain to Local") {
            val detail = fixtureMonkey.giveMeKotlinBuilder<TagDetail>()
                .sample()

            val result = detail.toLocal()

            result.title shouldBe detail.title
            result.description shouldBe detail.description
            result.color shouldBe detail.color
        }

        test("Local to Domain") {
            val entity = fixtureMonkey.giveMeKotlinBuilder<TagDetailLocalEntity>()
                .sample()

            val result = entity.toDomain()

            result.title shouldBe entity.title
            result.description shouldBe entity.description
            result.color shouldBe entity.color
        }
    }
}
