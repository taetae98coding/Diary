package io.github.taetae98coding.diary.core.mapper

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.github.taetae98coding.diary.core.database.api.entity.TagLocalEntity
import io.github.taetae98coding.diary.core.network.api.entity.TagRemoteEntity
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class TagRemoteMapperTest : FunSpec() {
    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        test("Local to Remote") {
            val entity = fixtureMonkey.giveMeKotlinBuilder<TagLocalEntity>()
                .sample()

            val result = entity.toRemote()

            result.id shouldBe entity.id
            result.detail shouldBe entity.detail.toRemote()
            result.isFinished shouldBe entity.isFinished
            result.isDeleted shouldBe entity.isDeleted
            result.updatedAt shouldBe entity.updatedAt
            result.createdAt shouldBe entity.createdAt
        }

        test("Remote to Local") {
            val entity = fixtureMonkey.giveMeKotlinBuilder<TagRemoteEntity>()
                .sample()

            val result = entity.toLocal()

            result.id shouldBe entity.id
            result.detail shouldBe entity.detail.toLocal()
            result.isFinished shouldBe entity.isFinished
            result.isDeleted shouldBe entity.isDeleted
            result.updatedAt shouldBe entity.updatedAt
            result.createdAt shouldBe entity.createdAt
        }
    }
}
