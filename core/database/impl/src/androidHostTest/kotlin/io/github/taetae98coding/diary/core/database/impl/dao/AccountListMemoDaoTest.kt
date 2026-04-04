package io.github.taetae98coding.diary.core.database.impl.dao

import android.app.Application
import androidx.paging.PagingSource
import androidx.room3.Room
import androidx.test.core.app.ApplicationProvider
import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.github.taetae98coding.diary.core.database.api.entity.AccountMemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.AccountTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.ListMemoFilterTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.MemoDetailLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.MemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.MemoTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.TagDetailLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.TagLocalEntity
import io.github.taetae98coding.diary.core.database.impl.DiaryDatabase
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlin.uuid.Uuid
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AccountListMemoDaoTest {
    private lateinit var database: DiaryDatabase
    private lateinit var accountListMemoDao: AccountListMemoDao
    private lateinit var memoDao: MemoDao
    private lateinit var accountMemoDao: AccountMemoDao
    private lateinit var tagDao: TagDao
    private lateinit var accountTagDao: AccountTagDao
    private lateinit var memoTagDao: MemoTagDao
    private lateinit var listMemoFilterTagDao: ListMemoFilterTagDao

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        database = Room.inMemoryDatabaseBuilder<DiaryDatabase>(context)
            .build()
        accountListMemoDao = database.accountListMemoDao()
        memoDao = database.memoDao()
        accountMemoDao = database.accountMemoDao()
        tagDao = database.tagDao()
        accountTagDao = database.accountTagDao()
        memoTagDao = database.memoTagDao()
        listMemoFilterTagDao = database.listMemoFilterTagDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    private suspend fun PagingSource<Int, MemoLocalEntity>.loadAll(): List<MemoLocalEntity> {
        val result = load(PagingSource.LoadParams.Refresh(null, 100, false))
        result.shouldBeInstanceOf<PagingSource.LoadResult.Page<Int, MemoLocalEntity>>()
        return result.data
    }

    @Test
    fun `메모를 조회한다`() = runTest {
        val accountId = Uuid.random()
        val memoId = Uuid.random()
        val memo = fixtureMonkey.giveMeKotlinBuilder<MemoLocalEntity>()
            .set(MemoLocalEntity::id, memoId)
            .set(
                MemoLocalEntity::detail,
                fixtureMonkey.giveMeKotlinBuilder<MemoDetailLocalEntity>()
                    .set(MemoDetailLocalEntity::start, LocalDateTime(2025, 6, 1, 0, 0))
                    .set(MemoDetailLocalEntity::endInclusive, LocalDateTime(2025, 6, 30, 23, 59))
                    .sample(),
            )
            .set(MemoLocalEntity::isFinished, false)
            .set(MemoLocalEntity::isDeleted, false)
            .sample()

        memoDao.upsert(memo)
        accountMemoDao.upsert(AccountMemoLocalEntity(accountId = accountId, memoId = memoId))

        val result = accountListMemoDao.page(accountId, tagPresence = 0, datePresence = 0).loadAll()

        result shouldHaveSize 1
        result[0].id shouldBe memoId
    }

    @Test
    fun `삭제된 메모는 조회하지 않는다`() = runTest {
        val accountId = Uuid.random()
        val memoId = Uuid.random()
        val memo = fixtureMonkey.giveMeKotlinBuilder<MemoLocalEntity>()
            .set(MemoLocalEntity::id, memoId)
            .set(
                MemoLocalEntity::detail,
                fixtureMonkey.giveMeKotlinBuilder<MemoDetailLocalEntity>()
                    .set(MemoDetailLocalEntity::start, LocalDateTime(2025, 6, 1, 0, 0))
                    .set(MemoDetailLocalEntity::endInclusive, LocalDateTime(2025, 6, 30, 23, 59))
                    .sample(),
            )
            .set(MemoLocalEntity::isFinished, false)
            .set(MemoLocalEntity::isDeleted, true)
            .sample()

        memoDao.upsert(memo)
        accountMemoDao.upsert(AccountMemoLocalEntity(accountId = accountId, memoId = memoId))

        val result = accountListMemoDao.page(accountId, tagPresence = 0, datePresence = 0).loadAll()

        result.shouldBeEmpty()
    }

    @Test
    fun `완료된 메모는 조회하지 않는다`() = runTest {
        val accountId = Uuid.random()
        val memoId = Uuid.random()
        val memo = fixtureMonkey.giveMeKotlinBuilder<MemoLocalEntity>()
            .set(MemoLocalEntity::id, memoId)
            .set(
                MemoLocalEntity::detail,
                fixtureMonkey.giveMeKotlinBuilder<MemoDetailLocalEntity>()
                    .set(MemoDetailLocalEntity::start, LocalDateTime(2025, 6, 1, 0, 0))
                    .set(MemoDetailLocalEntity::endInclusive, LocalDateTime(2025, 6, 30, 23, 59))
                    .sample(),
            )
            .set(MemoLocalEntity::isFinished, true)
            .set(MemoLocalEntity::isDeleted, false)
            .sample()

        memoDao.upsert(memo)
        accountMemoDao.upsert(AccountMemoLocalEntity(accountId = accountId, memoId = memoId))

        val result = accountListMemoDao.page(accountId, tagPresence = 0, datePresence = 0).loadAll()

        result.shouldBeEmpty()
    }

    @Test
    fun `다른 계정의 메모는 조회하지 않는다`() = runTest {
        val accountId = Uuid.random()
        val otherAccountId = Uuid.random()
        val memoId = Uuid.random()
        val memo = fixtureMonkey.giveMeKotlinBuilder<MemoLocalEntity>()
            .set(MemoLocalEntity::id, memoId)
            .set(
                MemoLocalEntity::detail,
                fixtureMonkey.giveMeKotlinBuilder<MemoDetailLocalEntity>()
                    .set(MemoDetailLocalEntity::start, LocalDateTime(2025, 6, 1, 0, 0))
                    .set(MemoDetailLocalEntity::endInclusive, LocalDateTime(2025, 6, 30, 23, 59))
                    .sample(),
            )
            .set(MemoLocalEntity::isFinished, false)
            .set(MemoLocalEntity::isDeleted, false)
            .sample()

        memoDao.upsert(memo)
        accountMemoDao.upsert(AccountMemoLocalEntity(accountId = otherAccountId, memoId = memoId))

        val result = accountListMemoDao.page(accountId, tagPresence = 0, datePresence = 0).loadAll()

        result.shouldBeEmpty()
    }

    @Test
    fun `start, endInclusive, title 순으로 정렬한다`() = runTest {
        val accountId = Uuid.random()
        val memoA = Uuid.random()
        val memoB = Uuid.random()
        val memoC = Uuid.random()

        memoDao.upsert(
            fixtureMonkey.giveMeKotlinBuilder<MemoLocalEntity>()
                .set(MemoLocalEntity::id, memoA)
                .set(
                    MemoLocalEntity::detail,
                    fixtureMonkey.giveMeKotlinBuilder<MemoDetailLocalEntity>()
                        .set(MemoDetailLocalEntity::start, LocalDateTime(2025, 3, 1, 0, 0))
                        .set(MemoDetailLocalEntity::endInclusive, LocalDateTime(2025, 3, 1, 23, 59))
                        .set(MemoDetailLocalEntity::title, "B")
                        .sample(),
                )
                .set(MemoLocalEntity::isFinished, false)
                .set(MemoLocalEntity::isDeleted, false)
                .sample(),
        )
        accountMemoDao.upsert(AccountMemoLocalEntity(accountId = accountId, memoId = memoA))

        memoDao.upsert(
            fixtureMonkey.giveMeKotlinBuilder<MemoLocalEntity>()
                .set(MemoLocalEntity::id, memoB)
                .set(
                    MemoLocalEntity::detail,
                    fixtureMonkey.giveMeKotlinBuilder<MemoDetailLocalEntity>()
                        .set(MemoDetailLocalEntity::start, LocalDateTime(2025, 2, 1, 0, 0))
                        .set(MemoDetailLocalEntity::endInclusive, LocalDateTime(2025, 2, 28, 23, 59))
                        .set(MemoDetailLocalEntity::title, "A")
                        .sample(),
                )
                .set(MemoLocalEntity::isFinished, false)
                .set(MemoLocalEntity::isDeleted, false)
                .sample(),
        )
        accountMemoDao.upsert(AccountMemoLocalEntity(accountId = accountId, memoId = memoB))

        memoDao.upsert(
            fixtureMonkey.giveMeKotlinBuilder<MemoLocalEntity>()
                .set(MemoLocalEntity::id, memoC)
                .set(
                    MemoLocalEntity::detail,
                    fixtureMonkey.giveMeKotlinBuilder<MemoDetailLocalEntity>()
                        .set(MemoDetailLocalEntity::start, LocalDateTime(2025, 3, 1, 0, 0))
                        .set(MemoDetailLocalEntity::endInclusive, LocalDateTime(2025, 3, 1, 23, 59))
                        .set(MemoDetailLocalEntity::title, "A")
                        .sample(),
                )
                .set(MemoLocalEntity::isFinished, false)
                .set(MemoLocalEntity::isDeleted, false)
                .sample(),
        )
        accountMemoDao.upsert(AccountMemoLocalEntity(accountId = accountId, memoId = memoC))

        val result = accountListMemoDao.page(accountId, tagPresence = 0, datePresence = 0).loadAll()

        result shouldHaveSize 3
        // start 순 → memoB(2월) 먼저
        result[0].id shouldBe memoB
        // 같은 start, endInclusive → title 순 → memoC("A") < memoA("B")
        result[1].id shouldBe memoC
        result[2].id shouldBe memoA
    }

    @Test
    fun `필터 태그가 없으면 모든 메모를 조회한다`() = runTest {
        val accountId = Uuid.random()
        val memoId = Uuid.random()
        val memo = fixtureMonkey.giveMeKotlinBuilder<MemoLocalEntity>()
            .set(MemoLocalEntity::id, memoId)
            .set(
                MemoLocalEntity::detail,
                fixtureMonkey.giveMeKotlinBuilder<MemoDetailLocalEntity>()
                    .set(MemoDetailLocalEntity::start, LocalDateTime(2025, 6, 1, 0, 0))
                    .set(MemoDetailLocalEntity::endInclusive, LocalDateTime(2025, 6, 30, 23, 59))
                    .sample(),
            )
            .set(MemoLocalEntity::isFinished, false)
            .set(MemoLocalEntity::isDeleted, false)
            .sample()

        memoDao.upsert(memo)
        accountMemoDao.upsert(AccountMemoLocalEntity(accountId = accountId, memoId = memoId))

        val result = accountListMemoDao.page(accountId, tagPresence = 0, datePresence = 0).loadAll()

        result shouldHaveSize 1
        result[0].id shouldBe memoId
    }

    @Test
    fun `필터 태그가 있으면 해당 태그가 달린 메모만 조회한다`() = runTest {
        val accountId = Uuid.random()
        val tagId = Uuid.random()
        val memoWithTag = Uuid.random()
        val memoWithoutTag = Uuid.random()

        tagDao.upsert(
            TagLocalEntity(
                id = tagId,
                detail = TagDetailLocalEntity(title = "filter", description = "", color = 0),
            ),
        )
        accountTagDao.upsert(AccountTagLocalEntity(accountId = accountId, tagId = tagId))
        listMemoFilterTagDao.upsert(ListMemoFilterTagLocalEntity(tagId = tagId))

        memoDao.upsert(
            fixtureMonkey.giveMeKotlinBuilder<MemoLocalEntity>()
                .set(MemoLocalEntity::id, memoWithTag)
                .set(
                    MemoLocalEntity::detail,
                    fixtureMonkey.giveMeKotlinBuilder<MemoDetailLocalEntity>()
                        .set(MemoDetailLocalEntity::start, LocalDateTime(2025, 6, 1, 0, 0))
                        .set(MemoDetailLocalEntity::endInclusive, LocalDateTime(2025, 6, 30, 23, 59))
                        .sample(),
                )
                .set(MemoLocalEntity::isFinished, false)
                .set(MemoLocalEntity::isDeleted, false)
                .sample(),
        )
        accountMemoDao.upsert(AccountMemoLocalEntity(accountId = accountId, memoId = memoWithTag))
        memoTagDao.upsert(MemoTagLocalEntity(memoId = memoWithTag, tagId = tagId, isMemoTag = true))

        memoDao.upsert(
            fixtureMonkey.giveMeKotlinBuilder<MemoLocalEntity>()
                .set(MemoLocalEntity::id, memoWithoutTag)
                .set(
                    MemoLocalEntity::detail,
                    fixtureMonkey.giveMeKotlinBuilder<MemoDetailLocalEntity>()
                        .set(MemoDetailLocalEntity::start, LocalDateTime(2025, 6, 1, 0, 0))
                        .set(MemoDetailLocalEntity::endInclusive, LocalDateTime(2025, 6, 30, 23, 59))
                        .sample(),
                )
                .set(MemoLocalEntity::isFinished, false)
                .set(MemoLocalEntity::isDeleted, false)
                .sample(),
        )
        accountMemoDao.upsert(AccountMemoLocalEntity(accountId = accountId, memoId = memoWithoutTag))

        val result = accountListMemoDao.page(accountId, tagPresence = 0, datePresence = 0).loadAll()

        result shouldHaveSize 1
        result[0].id shouldBe memoWithTag
    }

    @Test
    fun `삭제된 필터 태그는 필터로 동작하지 않는다`() = runTest {
        val accountId = Uuid.random()
        val tagId = Uuid.random()
        val memoId = Uuid.random()

        tagDao.upsert(
            TagLocalEntity(
                id = tagId,
                detail = TagDetailLocalEntity(title = "deleted", description = "", color = 0),
                isDeleted = true,
            ),
        )
        accountTagDao.upsert(AccountTagLocalEntity(accountId = accountId, tagId = tagId))
        listMemoFilterTagDao.upsert(ListMemoFilterTagLocalEntity(tagId = tagId))

        memoDao.upsert(
            fixtureMonkey.giveMeKotlinBuilder<MemoLocalEntity>()
                .set(MemoLocalEntity::id, memoId)
                .set(
                    MemoLocalEntity::detail,
                    fixtureMonkey.giveMeKotlinBuilder<MemoDetailLocalEntity>()
                        .set(MemoDetailLocalEntity::start, LocalDateTime(2025, 6, 1, 0, 0))
                        .set(MemoDetailLocalEntity::endInclusive, LocalDateTime(2025, 6, 30, 23, 59))
                        .sample(),
                )
                .set(MemoLocalEntity::isFinished, false)
                .set(MemoLocalEntity::isDeleted, false)
                .sample(),
        )
        accountMemoDao.upsert(AccountMemoLocalEntity(accountId = accountId, memoId = memoId))

        val result = accountListMemoDao.page(accountId, tagPresence = 0, datePresence = 0).loadAll()

        result shouldHaveSize 1
        result[0].id shouldBe memoId
    }

    @Test
    fun `완료된 필터 태그는 필터로 동작하지 않는다`() = runTest {
        val accountId = Uuid.random()
        val tagId = Uuid.random()
        val memoId = Uuid.random()

        tagDao.upsert(
            TagLocalEntity(
                id = tagId,
                detail = TagDetailLocalEntity(title = "finished", description = "", color = 0),
                isFinished = true,
            ),
        )
        accountTagDao.upsert(AccountTagLocalEntity(accountId = accountId, tagId = tagId))
        listMemoFilterTagDao.upsert(ListMemoFilterTagLocalEntity(tagId = tagId))

        memoDao.upsert(
            fixtureMonkey.giveMeKotlinBuilder<MemoLocalEntity>()
                .set(MemoLocalEntity::id, memoId)
                .set(
                    MemoLocalEntity::detail,
                    fixtureMonkey.giveMeKotlinBuilder<MemoDetailLocalEntity>()
                        .set(MemoDetailLocalEntity::start, LocalDateTime(2025, 6, 1, 0, 0))
                        .set(MemoDetailLocalEntity::endInclusive, LocalDateTime(2025, 6, 30, 23, 59))
                        .sample(),
                )
                .set(MemoLocalEntity::isFinished, false)
                .set(MemoLocalEntity::isDeleted, false)
                .sample(),
        )
        accountMemoDao.upsert(AccountMemoLocalEntity(accountId = accountId, memoId = memoId))

        val result = accountListMemoDao.page(accountId, tagPresence = 0, datePresence = 0).loadAll()

        result shouldHaveSize 1
        result[0].id shouldBe memoId
    }

    @Test
    fun `다른 계정의 필터 태그는 필터로 동작하지 않는다`() = runTest {
        val accountId = Uuid.random()
        val otherAccountId = Uuid.random()
        val tagId = Uuid.random()
        val memoId = Uuid.random()

        tagDao.upsert(
            TagLocalEntity(
                id = tagId,
                detail = TagDetailLocalEntity(title = "other", description = "", color = 0),
            ),
        )
        accountTagDao.upsert(AccountTagLocalEntity(accountId = otherAccountId, tagId = tagId))
        listMemoFilterTagDao.upsert(ListMemoFilterTagLocalEntity(tagId = tagId))

        memoDao.upsert(
            fixtureMonkey.giveMeKotlinBuilder<MemoLocalEntity>()
                .set(MemoLocalEntity::id, memoId)
                .set(
                    MemoLocalEntity::detail,
                    fixtureMonkey.giveMeKotlinBuilder<MemoDetailLocalEntity>()
                        .set(MemoDetailLocalEntity::start, LocalDateTime(2025, 6, 1, 0, 0))
                        .set(MemoDetailLocalEntity::endInclusive, LocalDateTime(2025, 6, 30, 23, 59))
                        .sample(),
                )
                .set(MemoLocalEntity::isFinished, false)
                .set(MemoLocalEntity::isDeleted, false)
                .sample(),
        )
        accountMemoDao.upsert(AccountMemoLocalEntity(accountId = accountId, memoId = memoId))

        val result = accountListMemoDao.page(accountId, tagPresence = 0, datePresence = 0).loadAll()

        result shouldHaveSize 1
        result[0].id shouldBe memoId
    }
}
