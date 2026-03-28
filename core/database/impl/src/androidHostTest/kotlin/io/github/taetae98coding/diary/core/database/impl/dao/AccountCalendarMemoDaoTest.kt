package io.github.taetae98coding.diary.core.database.impl.dao

import android.app.Application
import androidx.room3.Room
import androidx.test.core.app.ApplicationProvider
import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.github.taetae98coding.diary.core.database.api.entity.AccountMemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.MemoDetailLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.MemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.TagDetailLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.TagLocalEntity
import io.github.taetae98coding.diary.core.database.impl.DiaryDatabase
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AccountCalendarMemoDaoTest {
    private lateinit var database: DiaryDatabase
    private lateinit var accountCalendarMemoDao: AccountCalendarMemoDao
    private lateinit var memoDao: MemoDao
    private lateinit var accountMemoDao: AccountMemoDao
    private lateinit var tagDao: TagDao

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        database = Room.inMemoryDatabaseBuilder<DiaryDatabase>(context)
            .build()
        accountCalendarMemoDao = database.accountCalendarMemoDao()
        memoDao = database.memoDao()
        accountMemoDao = database.accountMemoDao()
        tagDao = database.tagDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `해당 연도의 메모를 조회한다`() = runTest {
        val accountId = Uuid.random()
        val memoId = Uuid.random()
        val memo = fixtureMonkey.giveMeKotlinBuilder<MemoLocalEntity>()
            .set(MemoLocalEntity::id, memoId)
            .set(
                MemoLocalEntity::detail,
                fixtureMonkey.giveMeKotlinBuilder<MemoDetailLocalEntity>()
                    .set(MemoDetailLocalEntity::start, LocalDateTime(2025, 3, 1, 0, 0))
                    .set(MemoDetailLocalEntity::endInclusive, LocalDateTime(2025, 3, 31, 23, 59))
                    .sample(),
            )
            .set(MemoLocalEntity::isDeleted, false)
            .sample()

        memoDao.upsert(memo)
        accountMemoDao.upsert(AccountMemoLocalEntity(accountId = accountId, memoId = memoId))

        val result = accountCalendarMemoDao.get(accountId, 2025).first()

        result shouldHaveSize 1
        result[0].id shouldBe memoId
    }

    @Test
    fun `연도를 걸쳐있는 메모를 조회한다`() = runTest {
        val accountId = Uuid.random()
        val memoId = Uuid.random()
        val memo = fixtureMonkey.giveMeKotlinBuilder<MemoLocalEntity>()
            .set(MemoLocalEntity::id, memoId)
            .set(
                MemoLocalEntity::detail,
                fixtureMonkey.giveMeKotlinBuilder<MemoDetailLocalEntity>()
                    .set(MemoDetailLocalEntity::start, LocalDateTime(2024, 12, 1, 0, 0))
                    .set(MemoDetailLocalEntity::endInclusive, LocalDateTime(2025, 2, 28, 23, 59))
                    .sample(),
            )
            .set(MemoLocalEntity::isDeleted, false)
            .sample()

        memoDao.upsert(memo)
        accountMemoDao.upsert(AccountMemoLocalEntity(accountId = accountId, memoId = memoId))

        val result = accountCalendarMemoDao.get(accountId, 2025).first()

        result shouldHaveSize 1
        result[0].id shouldBe memoId
    }

    @Test
    fun `해당 연도에 포함되지 않는 메모는 조회하지 않는다`() = runTest {
        val accountId = Uuid.random()
        val memoId = Uuid.random()
        val memo = fixtureMonkey.giveMeKotlinBuilder<MemoLocalEntity>()
            .set(MemoLocalEntity::id, memoId)
            .set(
                MemoLocalEntity::detail,
                fixtureMonkey.giveMeKotlinBuilder<MemoDetailLocalEntity>()
                    .set(MemoDetailLocalEntity::start, LocalDateTime(2024, 1, 1, 0, 0))
                    .set(MemoDetailLocalEntity::endInclusive, LocalDateTime(2024, 12, 31, 23, 59))
                    .sample(),
            )
            .set(MemoLocalEntity::isDeleted, false)
            .sample()

        memoDao.upsert(memo)
        accountMemoDao.upsert(AccountMemoLocalEntity(accountId = accountId, memoId = memoId))

        val result = accountCalendarMemoDao.get(accountId, 2025).first()

        result.shouldBeEmpty()
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
            .set(MemoLocalEntity::isDeleted, true)
            .sample()

        memoDao.upsert(memo)
        accountMemoDao.upsert(AccountMemoLocalEntity(accountId = accountId, memoId = memoId))

        val result = accountCalendarMemoDao.get(accountId, 2025).first()

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
            .set(MemoLocalEntity::isDeleted, false)
            .sample()

        memoDao.upsert(memo)
        accountMemoDao.upsert(AccountMemoLocalEntity(accountId = otherAccountId, memoId = memoId))

        val result = accountCalendarMemoDao.get(accountId, 2025).first()

        result.shouldBeEmpty()
    }

    @Test
    fun `isAllDay DESC, start, title 순으로 정렬한다`() = runTest {
        val accountId = Uuid.random()

        val memoA = Uuid.random()
        val memoB = Uuid.random()
        val memoC = Uuid.random()

        // memoA: isAllDay = false, start = 3월 1일, title = "B"
        memoDao.upsert(
            fixtureMonkey.giveMeKotlinBuilder<MemoLocalEntity>()
                .set(MemoLocalEntity::id, memoA)
                .set(
                    MemoLocalEntity::detail,
                    fixtureMonkey.giveMeKotlinBuilder<MemoDetailLocalEntity>()
                        .set(MemoDetailLocalEntity::isAllDay, false)
                        .set(MemoDetailLocalEntity::start, LocalDateTime(2025, 3, 1, 0, 0))
                        .set(MemoDetailLocalEntity::endInclusive, LocalDateTime(2025, 3, 1, 23, 59))
                        .set(MemoDetailLocalEntity::title, "B")
                        .sample(),
                )
                .set(MemoLocalEntity::isDeleted, false)
                .sample(),
        )
        accountMemoDao.upsert(AccountMemoLocalEntity(accountId = accountId, memoId = memoA))

        // memoB: isAllDay = true, start = 3월 15일, title = "A"
        memoDao.upsert(
            fixtureMonkey.giveMeKotlinBuilder<MemoLocalEntity>()
                .set(MemoLocalEntity::id, memoB)
                .set(
                    MemoLocalEntity::detail,
                    fixtureMonkey.giveMeKotlinBuilder<MemoDetailLocalEntity>()
                        .set(MemoDetailLocalEntity::isAllDay, true)
                        .set(MemoDetailLocalEntity::start, LocalDateTime(2025, 3, 15, 0, 0))
                        .set(MemoDetailLocalEntity::endInclusive, LocalDateTime(2025, 3, 15, 23, 59))
                        .set(MemoDetailLocalEntity::title, "A")
                        .sample(),
                )
                .set(MemoLocalEntity::isDeleted, false)
                .sample(),
        )
        accountMemoDao.upsert(AccountMemoLocalEntity(accountId = accountId, memoId = memoB))

        // memoC: isAllDay = false, start = 3월 1일, title = "A"
        memoDao.upsert(
            fixtureMonkey.giveMeKotlinBuilder<MemoLocalEntity>()
                .set(MemoLocalEntity::id, memoC)
                .set(
                    MemoLocalEntity::detail,
                    fixtureMonkey.giveMeKotlinBuilder<MemoDetailLocalEntity>()
                        .set(MemoDetailLocalEntity::isAllDay, false)
                        .set(MemoDetailLocalEntity::start, LocalDateTime(2025, 3, 1, 0, 0))
                        .set(MemoDetailLocalEntity::endInclusive, LocalDateTime(2025, 3, 1, 23, 59))
                        .set(MemoDetailLocalEntity::title, "A")
                        .sample(),
                )
                .set(MemoLocalEntity::isDeleted, false)
                .sample(),
        )
        accountMemoDao.upsert(AccountMemoLocalEntity(accountId = accountId, memoId = memoC))

        val result = accountCalendarMemoDao.get(accountId, 2025).first()

        result shouldHaveSize 3
        // isAllDay DESC → memoB(true) 먼저
        result[0].id shouldBe memoB
        // 같은 start → title 순 → memoC("A") < memoA("B")
        result[1].id shouldBe memoC
        result[2].id shouldBe memoA
    }

    @Test
    fun `primaryTag가 있는 경우 memo의 color가 아니라 primaryTag의 color를 사용한다`() = runTest {
        val accountId = Uuid.random()
        val memoId = Uuid.random()
        val tagId = Uuid.random()
        val memoColor = 0xFF0000
        val tagColor = 0x00FF00

        tagDao.upsert(
            TagLocalEntity(
                id = tagId,
                detail = TagDetailLocalEntity(
                    title = "tag",
                    description = "",
                    color = tagColor,
                ),
            ),
        )

        val memo = fixtureMonkey.giveMeKotlinBuilder<MemoLocalEntity>()
            .set(MemoLocalEntity::id, memoId)
            .set(
                MemoLocalEntity::detail,
                fixtureMonkey.giveMeKotlinBuilder<MemoDetailLocalEntity>()
                    .set(MemoDetailLocalEntity::start, LocalDateTime(2025, 6, 1, 0, 0))
                    .set(MemoDetailLocalEntity::endInclusive, LocalDateTime(2025, 6, 30, 23, 59))
                    .set(MemoDetailLocalEntity::color, memoColor)
                    .sample(),
            )
            .set(MemoLocalEntity::primaryTag, tagId)
            .set(MemoLocalEntity::isDeleted, false)
            .sample()

        memoDao.upsert(memo)
        accountMemoDao.upsert(AccountMemoLocalEntity(accountId = accountId, memoId = memoId))

        val result = accountCalendarMemoDao.get(accountId, 2025).first()

        result shouldHaveSize 1
        result[0].color shouldBe tagColor
    }
}
