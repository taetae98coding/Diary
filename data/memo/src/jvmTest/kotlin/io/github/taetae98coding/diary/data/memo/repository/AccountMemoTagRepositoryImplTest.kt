package io.github.taetae98coding.diary.data.memo.repository

import io.github.taetae98coding.diary.core.database.api.DatabaseTransactor
import io.github.taetae98coding.diary.core.database.api.datasource.MemoLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.MemoTagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.SyncMemoLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.SyncMemoTagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.MemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.MemoTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncMemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncMemoTagLocalEntity
import io.kotest.core.spec.style.FunSpec
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import kotlin.time.Clock
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.flowOf

class AccountMemoTagRepositoryImplTest : FunSpec() {
    private val clock = mockk<Clock>(relaxed = true)
    private val databaseTransactor = mockk<DatabaseTransactor>()
    private val memoLocalDataSource = mockk<MemoLocalDataSource>(relaxUnitFun = true)
    private val memoTagLocalDataSource = mockk<MemoTagLocalDataSource>(relaxUnitFun = true)
    private val syncMemoLocalDataSource = mockk<SyncMemoLocalDataSource>(relaxUnitFun = true)
    private val syncMemoTagLocalDataSource = mockk<SyncMemoTagLocalDataSource>(relaxUnitFun = true)
    private val repository = AccountMemoTagRepositoryImpl(
        clock,
        databaseTransactor,
        memoLocalDataSource,
        memoTagLocalDataSource,
        syncMemoLocalDataSource,
        syncMemoTagLocalDataSource,
    )

    init {
        beforeTest { clearAllMocks() }

        test("updatePrimaryTag - primaryTag가 non-null이면 memoTag와 syncMemoTag도 저장한다") {
            val memoId = Uuid.random()
            val primaryTag = Uuid.random()

            coEvery { databaseTransactor.writeTransaction(any<suspend () -> Unit>()) } coAnswers {
                firstArg<suspend () -> Unit>().invoke()
            }

            repository.updatePrimaryTag(memoId, primaryTag)

            coVerifyOrder {
                memoLocalDataSource.updatePrimaryTag(memoId, primaryTag, any())
                memoTagLocalDataSource.upsert(any<List<MemoTagLocalEntity>>())
                syncMemoTagLocalDataSource.upsert(any<SyncMemoTagLocalEntity>())
                syncMemoLocalDataSource.upsert(SyncMemoLocalEntity(memoId = memoId))
            }
        }

        test("updatePrimaryTag - primaryTag가 null이면 memoTag를 저장하지 않는다") {
            val memoId = Uuid.random()

            coEvery { databaseTransactor.writeTransaction(any<suspend () -> Unit>()) } coAnswers {
                firstArg<suspend () -> Unit>().invoke()
            }

            repository.updatePrimaryTag(memoId, primaryTag = null)

            coVerifyOrder {
                memoLocalDataSource.updatePrimaryTag(memoId, null, any())
                syncMemoLocalDataSource.upsert(SyncMemoLocalEntity(memoId = memoId))
            }
            coVerify(exactly = 0) { memoTagLocalDataSource.upsert(any<List<MemoTagLocalEntity>>()) }
            coVerify(exactly = 0) { syncMemoTagLocalDataSource.upsert(any<SyncMemoTagLocalEntity>()) }
        }

        test("upsertMemoTag - isMemoTag가 true이면 memoTag를 저장하고 primaryTag를 변경하지 않는다") {
            val memoId = Uuid.random()
            val tagId = Uuid.random()

            coEvery { databaseTransactor.writeTransaction(any<suspend () -> Unit>()) } coAnswers {
                firstArg<suspend () -> Unit>().invoke()
            }

            repository.upsertMemoTag(memoId, tagId, isMemoTag = true)

            coVerifyOrder {
                memoTagLocalDataSource.upsert(any<List<MemoTagLocalEntity>>())
                syncMemoTagLocalDataSource.upsert(SyncMemoTagLocalEntity(memoId = memoId, tagId = tagId))
            }
            coVerify(exactly = 0) { memoLocalDataSource.get(any()) }
            coVerify(exactly = 0) { memoLocalDataSource.updatePrimaryTag(any(), any(), any()) }
        }

        test("upsertMemoTag - isMemoTag가 false이고 primaryTag와 같으면 primaryTag를 null로 변경한다") {
            val memoId = Uuid.random()
            val tagId = Uuid.random()
            val memo = mockk<MemoLocalEntity>()
            every { memo.primaryTag } returns tagId

            coEvery { databaseTransactor.writeTransaction(any<suspend () -> Unit>()) } coAnswers {
                firstArg<suspend () -> Unit>().invoke()
            }
            every { memoLocalDataSource.get(memoId) } returns flowOf(memo)

            repository.upsertMemoTag(memoId, tagId, isMemoTag = false)

            coVerifyOrder {
                memoTagLocalDataSource.upsert(any<List<MemoTagLocalEntity>>())
                memoLocalDataSource.get(memoId)
                memoLocalDataSource.updatePrimaryTag(memoId, null, any())
                syncMemoLocalDataSource.upsert(SyncMemoLocalEntity(memoId = memoId))
                syncMemoTagLocalDataSource.upsert(SyncMemoTagLocalEntity(memoId = memoId, tagId = tagId))
            }
        }

        test("upsertMemoTag - isMemoTag가 false이고 primaryTag와 다르면 primaryTag를 변경하지 않는다") {
            val memoId = Uuid.random()
            val tagId = Uuid.random()
            val memo = mockk<MemoLocalEntity>()
            every { memo.primaryTag } returns Uuid.random()

            coEvery { databaseTransactor.writeTransaction(any<suspend () -> Unit>()) } coAnswers {
                firstArg<suspend () -> Unit>().invoke()
            }
            every { memoLocalDataSource.get(memoId) } returns flowOf(memo)

            repository.upsertMemoTag(memoId, tagId, isMemoTag = false)

            coVerify(exactly = 0) { memoLocalDataSource.updatePrimaryTag(any(), any(), any()) }
            coVerify(exactly = 0) { syncMemoLocalDataSource.upsert(any<SyncMemoLocalEntity>()) }
        }
    }
}
