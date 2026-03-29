package io.github.taetae98coding.diary.core.mapper

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.github.taetae98coding.diary.core.database.api.entity.TagLocalEntity
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class TagMapperTest : FunSpec() {
    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        test("Local to Domain") {
            val entity = fixtureMonkey.giveMeKotlinBuilder<TagLocalEntity>()
                .sample()

            val result = entity.toDomain()

            result.id shouldBe entity.id
            result.detail shouldBe entity.detail.toDomain()
            result.isFinished shouldBe entity.isFinished
            result.isDeleted shouldBe entity.isDeleted
            result.updatedAt shouldBe entity.updatedAt
            result.createdAt shouldBe entity.createdAt
        }
    }
}
