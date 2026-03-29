package io.github.taetae98coding.diary.core.mapper

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.github.taetae98coding.diary.core.database.api.entity.TagDetailLocalEntity
import io.github.taetae98coding.diary.core.network.api.entity.TagDetailRemoteEntity
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class TagDetailRemoteMapperTest : FunSpec() {
    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        test("Local to Remote") {
            val entity = fixtureMonkey.giveMeKotlinBuilder<TagDetailLocalEntity>()
                .sample()

            val result = entity.toRemote()

            result.title shouldBe entity.title
            result.description shouldBe entity.description
            result.color shouldBe entity.color
        }

        test("Remote to Local") {
            val entity = fixtureMonkey.giveMeKotlinBuilder<TagDetailRemoteEntity>()
                .sample()

            val result = entity.toLocal()

            result.title shouldBe entity.title
            result.description shouldBe entity.description
            result.color shouldBe entity.color
        }
    }
}
