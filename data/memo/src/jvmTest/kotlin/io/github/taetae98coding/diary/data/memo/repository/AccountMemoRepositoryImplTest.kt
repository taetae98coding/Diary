package io.github.taetae98coding.diary.data.memo.repository

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import io.github.taetae98coding.diary.core.database.api.DatabaseTransactor
import io.github.taetae98coding.diary.core.database.api.datasource.AccountMemoLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.MemoLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.SyncMemoLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.AccountMemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.MemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncMemoLocalEntity
import io.github.taetae98coding.diary.core.mapper.toLocal
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.mockk
import io.mockk.slot
import kotlin.uuid.Uuid

class AccountMemoRepositoryImplTest : FunSpec() {
    private val databaseTransactor = mockk<DatabaseTransactor>()
    private val memoLocalDataSource = mockk<MemoLocalDataSource>(relaxUnitFun = true)
    private val accountMemoLocalDataSource = mockk<AccountMemoLocalDataSource>(relaxUnitFun = true)
    private val syncMemoLocalDataSource = mockk<SyncMemoLocalDataSource>(relaxUnitFun = true)
    private val repository = AccountMemoRepositoryImpl(
        databaseTransactor,
        memoLocalDataSource,
        accountMemoLocalDataSource,
        syncMemoLocalDataSource,
    )

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        beforeTest { clearAllMocks() }

        test("add") {
            val accountId = Uuid.random()
            val detail = fixtureMonkey.giveMeOne<MemoDetail>()
            val memoSlot = slot<MemoLocalEntity>()
            val accountMemoSlot = slot<AccountMemoLocalEntity>()
            val syncMemoSlot = slot<SyncMemoLocalEntity>()

            coEvery { databaseTransactor.writeTransaction(any<suspend () -> Unit>()) } coAnswers {
                firstArg<suspend () -> Unit>().invoke()
            }

            coEvery { memoLocalDataSource.upsert(capture(memoSlot)) } returns Unit
            coEvery { accountMemoLocalDataSource.upsert(capture(accountMemoSlot)) } returns Unit
            coEvery { syncMemoLocalDataSource.upsert(capture(syncMemoSlot)) } returns Unit

            repository.add(accountId, detail)

            memoSlot.captured.detail shouldBe detail.toLocal()
            accountMemoSlot.captured.accountId shouldBe accountId
            accountMemoSlot.captured.memoId shouldBe memoSlot.captured.id
            syncMemoSlot.captured.memoId shouldBe memoSlot.captured.id
        }

        test("add - transaction 내에서 순서대로 호출한다") {
            val accountId = Uuid.random()
            val detail = fixtureMonkey.giveMeOne<MemoDetail>()

            coEvery { databaseTransactor.writeTransaction(any<suspend () -> Unit>()) } coAnswers {
                firstArg<suspend () -> Unit>().invoke()
            }

            repository.add(accountId, detail)

            coVerifyOrder {
                memoLocalDataSource.upsert(any<MemoLocalEntity>())
                accountMemoLocalDataSource.upsert(any<AccountMemoLocalEntity>())
                syncMemoLocalDataSource.upsert(any<SyncMemoLocalEntity>())
            }
        }

        test("updateDetail") {
            val memoId = Uuid.random()
            val detail = fixtureMonkey.giveMeOne<MemoDetail>()

            coEvery { databaseTransactor.writeTransaction(any<suspend () -> Unit>()) } coAnswers {
                firstArg<suspend () -> Unit>().invoke()
            }

            repository.updateDetail(memoId, detail)

            coVerifyOrder {
                memoLocalDataSource.updateDetail(memoId, detail.toLocal(), any())
                syncMemoLocalDataSource.upsert(SyncMemoLocalEntity(memoId = memoId))
            }
        }

        test("updateFinish") {
            val memoId = Uuid.random()
            val isFinished = true

            coEvery { databaseTransactor.writeTransaction(any<suspend () -> Unit>()) } coAnswers {
                firstArg<suspend () -> Unit>().invoke()
            }

            repository.updateFinish(memoId, isFinished)

            coVerifyOrder {
                memoLocalDataSource.updateFinish(memoId, isFinished, any())
                syncMemoLocalDataSource.upsert(SyncMemoLocalEntity(memoId = memoId))
            }
        }

        test("updateDelete") {
            val memoId = Uuid.random()
            val isDeleted = true

            coEvery { databaseTransactor.writeTransaction(any<suspend () -> Unit>()) } coAnswers {
                firstArg<suspend () -> Unit>().invoke()
            }

            repository.updateDelete(memoId, isDeleted)

            coVerifyOrder {
                memoLocalDataSource.updateDelete(memoId, isDeleted, any())
                syncMemoLocalDataSource.upsert(SyncMemoLocalEntity(memoId = memoId))
            }
        }
    }
}
