package io.github.taetae98coding.diary.core.mapper

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.github.taetae98coding.diary.core.database.api.entity.MemoLocalEntity
import io.github.taetae98coding.diary.core.network.api.entity.MemoRemoteEntity
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDateTime

class MemoRemoteMapperTest : FunSpec() {
    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        test("Local to Remote") {
            val entity = fixtureMonkey.giveMeKotlinBuilder<MemoLocalEntity>()
                .set("detail.start", LocalDateTime(2026, 1, 1, 0, 0))
                .set("detail.endInclusive", LocalDateTime(2026, 1, 2, 0, 0))
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
            val entity = fixtureMonkey.giveMeKotlinBuilder<MemoRemoteEntity>()
                .set("detail.start", LocalDateTime(2026, 1, 1, 0, 0))
                .set("detail.endInclusive", LocalDateTime(2026, 1, 2, 0, 0))
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
