package io.github.taetae98coding.diary.core.database.impl.dao

import android.app.Application
import androidx.room3.Room
import androidx.test.core.app.ApplicationProvider
import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.github.taetae98coding.diary.core.database.api.entity.RRuleDiaryByDayLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.RoutineDetailLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.RoutineLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.RoutineRRuleLocalEntity
import io.github.taetae98coding.diary.core.database.impl.DiaryDatabase
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RoutineDaoTest {
    private lateinit var database: DiaryDatabase
    private lateinit var routineDao: RoutineDao

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        database = Room.inMemoryDatabaseBuilder<DiaryDatabase>(context).build()
        routineDao = database.routineDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `Routine을 저장하고 로드한다`() = runTest {
        val id = Uuid.random()
        val entity = fixtureMonkey.giveMeKotlinBuilder<RoutineLocalEntity>()
            .set(RoutineLocalEntity::id, id)
            .set(
                RoutineLocalEntity::detail,
                fixtureMonkey.giveMeKotlinBuilder<RoutineDetailLocalEntity>()
                    .set(RoutineDetailLocalEntity::title, "운동")
                    .set(RoutineDetailLocalEntity::description, "매일 운동")
                    .sample(),
            )
            .sample()

        routineDao.upsert(entity)

        val result = routineDao.get(id).first()

        result shouldNotBe null
        result!!.id shouldBe id
        result.detail.title shouldBe "운동"
        result.detail.description shouldBe "매일 운동"
    }

    @Test
    fun `RRule 빈 리스트로 저장하고 로드한다`() = runTest {
        val id = Uuid.random()
        val entity = fixtureMonkey.giveMeKotlinBuilder<RoutineLocalEntity>()
            .set(RoutineLocalEntity::id, id)
            .set(RoutineLocalEntity::rRules, emptyList<RoutineRRuleLocalEntity>())
            .sample()

        routineDao.upsert(entity)

        val result = routineDao.get(id).first()

        result!!.rRules.shouldBeEmpty()
    }

    @Test
    fun `RRule byDay만 있는 항목을 JSON으로 저장하고 로드한다`() = runTest {
        val id = Uuid.random()
        val rRule = RoutineRRuleLocalEntity(
            diaryByDay = RRuleDiaryByDayLocalEntity(
                days = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
                ordinal = null,
            ),
            byMonthDay = emptyList(),
        )
        val entity = fixtureMonkey.giveMeKotlinBuilder<RoutineLocalEntity>()
            .set(RoutineLocalEntity::id, id)
            .set(RoutineLocalEntity::rRules, listOf(rRule))
            .sample()

        routineDao.upsert(entity)

        val result = routineDao.get(id).first()

        result!!.rRules shouldHaveSize 1
        result.rRules[0].diaryByDay.days shouldContainExactlyInAnyOrder listOf(
            DayOfWeek.MONDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.FRIDAY,
        )
        result.rRules[0].diaryByDay.ordinal shouldBe null
        result.rRules[0].byMonthDay.shouldBeEmpty()
    }

    @Test
    fun `RRule byMonthDay만 있는 항목을 JSON으로 저장하고 로드한다`() = runTest {
        val id = Uuid.random()
        val rRule = RoutineRRuleLocalEntity(
            diaryByDay = RRuleDiaryByDayLocalEntity(),
            byMonthDay = listOf(1, 15, -1),
        )
        val entity = fixtureMonkey.giveMeKotlinBuilder<RoutineLocalEntity>()
            .set(RoutineLocalEntity::id, id)
            .set(RoutineLocalEntity::rRules, listOf(rRule))
            .sample()

        routineDao.upsert(entity)

        val result = routineDao.get(id).first()

        result!!.rRules shouldHaveSize 1
        result.rRules[0].diaryByDay.days.shouldBeEmpty()
        result.rRules[0].byMonthDay shouldContainExactlyInAnyOrder listOf(1, 15, -1)
    }

    @Test
    fun `RRule byDay에 ordinal이 있으면 라운드트립으로 보존된다`() = runTest {
        val id = Uuid.random()
        val rRule = RoutineRRuleLocalEntity(
            diaryByDay = RRuleDiaryByDayLocalEntity(
                days = listOf(DayOfWeek.TUESDAY),
                ordinal = 3,
            ),
        )
        val entity = fixtureMonkey.giveMeKotlinBuilder<RoutineLocalEntity>()
            .set(RoutineLocalEntity::id, id)
            .set(RoutineLocalEntity::rRules, listOf(rRule))
            .sample()

        routineDao.upsert(entity)

        val result = routineDao.get(id).first()

        result!!.rRules[0].diaryByDay.ordinal shouldBe 3
        result.rRules[0].diaryByDay.days shouldBe listOf(DayOfWeek.TUESDAY)
    }

    @Test
    fun `RRule 여러 항목을 JSON 리스트로 저장하고 순서를 유지한다`() = runTest {
        val id = Uuid.random()
        val rRules = listOf(
            RoutineRRuleLocalEntity(
                diaryByDay = RRuleDiaryByDayLocalEntity(days = listOf(DayOfWeek.MONDAY)),
            ),
            RoutineRRuleLocalEntity(
                diaryByDay = RRuleDiaryByDayLocalEntity(),
                byMonthDay = listOf(15),
            ),
            RoutineRRuleLocalEntity(
                diaryByDay = RRuleDiaryByDayLocalEntity(
                    days = listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY),
                    ordinal = -1,
                ),
            ),
        )
        val entity = fixtureMonkey.giveMeKotlinBuilder<RoutineLocalEntity>()
            .set(RoutineLocalEntity::id, id)
            .set(RoutineLocalEntity::rRules, rRules)
            .sample()

        routineDao.upsert(entity)

        val result = routineDao.get(id).first()

        result!!.rRules shouldHaveSize 3
        result.rRules[0].diaryByDay.days shouldBe listOf(DayOfWeek.MONDAY)
        result.rRules[1].byMonthDay shouldBe listOf(15)
        result.rRules[2].diaryByDay.ordinal shouldBe -1
        result.rRules[2].diaryByDay.days shouldContainExactlyInAnyOrder listOf(
            DayOfWeek.SATURDAY,
            DayOfWeek.SUNDAY,
        )
    }

    @Test
    fun `RoutineDetail의 nullable 필드들을 정상 저장한다`() = runTest {
        val id = Uuid.random()
        val entity = fixtureMonkey.giveMeKotlinBuilder<RoutineLocalEntity>()
            .set(RoutineLocalEntity::id, id)
            .set(
                RoutineLocalEntity::detail,
                fixtureMonkey.giveMeKotlinBuilder<RoutineDetailLocalEntity>()
                    .set(RoutineDetailLocalEntity::start, LocalDate(2026, 4, 25))
                    .set(RoutineDetailLocalEntity::endInclusive, null)
                    .set(RoutineDetailLocalEntity::routineCount, 7)
                    .sample(),
            )
            .sample()

        routineDao.upsert(entity)

        val result = routineDao.get(id).first()

        result!!.detail.start shouldBe LocalDate(2026, 4, 25)
        result.detail.endInclusive shouldBe null
        result.detail.routineCount shouldBe 7
    }

    @Test
    fun `updateDetail은 상세 정보와 updatedAt을 갱신한다`() = runTest {
        val id = Uuid.random()
        val entity = fixtureMonkey.giveMeKotlinBuilder<RoutineLocalEntity>()
            .set(RoutineLocalEntity::id, id)
            .sample()
        routineDao.upsert(entity)

        routineDao.updateDetail(
            routineId = id,
            title = "new",
            description = "new desc",
            start = LocalDate(2026, 5, 1),
            endInclusive = LocalDate(2026, 5, 31),
            color = 0x00FF00,
            routineCount = 5,
            updatedAt = 2000L,
        )

        val result = routineDao.get(id).first()!!
        result.detail.title shouldBe "new"
        result.detail.description shouldBe "new desc"
        result.detail.color shouldBe 0x00FF00
        result.detail.routineCount shouldBe 5
        result.updatedAt shouldBe 2000L
    }

    @Test
    fun `updateFinish는 isFinished 플래그를 갱신한다`() = runTest {
        val id = Uuid.random()
        val entity = fixtureMonkey.giveMeKotlinBuilder<RoutineLocalEntity>()
            .set(RoutineLocalEntity::id, id)
            .set(RoutineLocalEntity::isFinished, false)
            .sample()
        routineDao.upsert(entity)

        routineDao.updateFinish(id, isFinished = true, updatedAt = 3000L)

        val result = routineDao.get(id).first()!!
        result.isFinished shouldBe true
        result.updatedAt shouldBe 3000L
    }

    @Test
    fun `updateDelete는 isDeleted 플래그를 갱신한다`() = runTest {
        val id = Uuid.random()
        val entity = fixtureMonkey.giveMeKotlinBuilder<RoutineLocalEntity>()
            .set(RoutineLocalEntity::id, id)
            .set(RoutineLocalEntity::isDeleted, false)
            .sample()
        routineDao.upsert(entity)

        routineDao.updateDelete(id, isDeleted = true, updatedAt = 4000L)

        val result = routineDao.get(id).first()!!
        result.isDeleted shouldBe true
        result.updatedAt shouldBe 4000L
    }

    @Test
    fun `존재하지 않는 routineId 조회 시 null을 반환한다`() = runTest {
        val result = routineDao.get(Uuid.random()).first()

        result shouldBe null
    }
}
