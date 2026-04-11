package io.github.taetae98coding.diary.data.tag.repository

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import io.github.taetae98coding.diary.core.database.api.DatabaseTransactor
import io.github.taetae98coding.diary.core.database.api.datasource.AccountTagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.SyncTagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.TagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.AccountTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.TagLocalEntity
import io.github.taetae98coding.diary.core.mapper.toLocal
import io.github.taetae98coding.diary.core.model.tag.TagDetail
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.mockk
import io.mockk.slot
import kotlin.time.Clock
import kotlin.uuid.Uuid

class AccountTagRepositoryImplTest : FunSpec() {
    private val clock = mockk<Clock>(relaxed = true)
    private val databaseTransactor = mockk<DatabaseTransactor>()
    private val tagLocalDataSource = mockk<TagLocalDataSource>(relaxUnitFun = true)
    private val accountTagLocalDataSource = mockk<AccountTagLocalDataSource>(relaxUnitFun = true)
    private val syncTagLocalDataSource = mockk<SyncTagLocalDataSource>(relaxUnitFun = true)
    private val repository = AccountTagRepositoryImpl(
        clock,
        databaseTransactor,
        tagLocalDataSource,
        accountTagLocalDataSource,
        syncTagLocalDataSource,
    )

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        beforeTest { clearAllMocks() }

        test("add") {
            val accountId = Uuid.random()
            val detail = fixtureMonkey.giveMeOne<TagDetail>()
            val tagSlot = slot<TagLocalEntity>()
            val accountTagSlot = slot<AccountTagLocalEntity>()
            val syncTagSlot = slot<SyncTagLocalEntity>()

            coEvery { databaseTransactor.writeTransaction(any<suspend () -> Unit>()) } coAnswers {
                firstArg<suspend () -> Unit>().invoke()
            }

            coEvery { tagLocalDataSource.upsert(capture(tagSlot)) } returns Unit
            coEvery { accountTagLocalDataSource.upsert(capture(accountTagSlot)) } returns Unit
            coEvery { syncTagLocalDataSource.upsert(capture(syncTagSlot)) } returns Unit

            repository.add(accountId, detail)

            tagSlot.captured.detail shouldBe detail.toLocal()
            accountTagSlot.captured.accountId shouldBe accountId
            accountTagSlot.captured.tagId shouldBe tagSlot.captured.id
            syncTagSlot.captured.tagId shouldBe tagSlot.captured.id
        }

        test("add - transaction 내에서 순서대로 호출한다") {
            val accountId = Uuid.random()
            val detail = fixtureMonkey.giveMeOne<TagDetail>()

            coEvery { databaseTransactor.writeTransaction(any<suspend () -> Unit>()) } coAnswers {
                firstArg<suspend () -> Unit>().invoke()
            }

            repository.add(accountId, detail)

            coVerifyOrder {
                tagLocalDataSource.upsert(any<TagLocalEntity>())
                accountTagLocalDataSource.upsert(any<AccountTagLocalEntity>())
                syncTagLocalDataSource.upsert(any<SyncTagLocalEntity>())
            }
        }

        test("updateDetail") {
            val tagId = Uuid.random()
            val detail = fixtureMonkey.giveMeOne<TagDetail>()

            coEvery { databaseTransactor.writeTransaction(any<suspend () -> Unit>()) } coAnswers {
                firstArg<suspend () -> Unit>().invoke()
            }

            repository.updateDetail(tagId, detail)

            coVerifyOrder {
                tagLocalDataSource.updateDetail(tagId, detail.toLocal(), any())
                syncTagLocalDataSource.upsert(SyncTagLocalEntity(tagId = tagId))
            }
        }

        test("updateFinish") {
            val tagId = Uuid.random()
            val isFinished = true

            coEvery { databaseTransactor.writeTransaction(any<suspend () -> Unit>()) } coAnswers {
                firstArg<suspend () -> Unit>().invoke()
            }

            repository.updateFinish(tagId, isFinished)

            coVerifyOrder {
                tagLocalDataSource.updateFinish(tagId, isFinished, any())
                syncTagLocalDataSource.upsert(SyncTagLocalEntity(tagId = tagId))
            }
        }

        test("updateDelete") {
            val tagId = Uuid.random()
            val isDeleted = true

            coEvery { databaseTransactor.writeTransaction(any<suspend () -> Unit>()) } coAnswers {
                firstArg<suspend () -> Unit>().invoke()
            }

            repository.updateDelete(tagId, isDeleted)

            coVerifyOrder {
                tagLocalDataSource.updateDelete(tagId, isDeleted, any())
                syncTagLocalDataSource.upsert(SyncTagLocalEntity(tagId = tagId))
            }
        }
    }
}
