package io.github.taetae98coding.diary.core.mapper

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.github.taetae98coding.diary.core.database.api.entity.MemoTagLocalEntity
import io.github.taetae98coding.diary.core.network.api.entity.MemoTagRemoteEntity
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class MemoTagRemoteMapperTest : FunSpec() {
    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        test("Local to Remote") {
            val entity = fixtureMonkey.giveMeKotlinBuilder<MemoTagLocalEntity>()
                .sample()

            val result = entity.toRemote()

            result.memoId shouldBe entity.memoId
            result.tagId shouldBe entity.tagId
            result.isMemoTag shouldBe entity.isMemoTag
            result.updatedAt shouldBe entity.updatedAt
        }

        test("Remote to Local") {
            val entity = fixtureMonkey.giveMeKotlinBuilder<MemoTagRemoteEntity>()
                .sample()

            val result = entity.toLocal()

            result.memoId shouldBe entity.memoId
            result.tagId shouldBe entity.tagId
            result.isMemoTag shouldBe entity.isMemoTag
            result.updatedAt shouldBe entity.updatedAt
        }
    }
}
