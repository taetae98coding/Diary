package io.github.taetae98coding.diary.data.routine.repository

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import io.github.taetae98coding.diary.core.database.api.DatabaseTransactor
import io.github.taetae98coding.diary.core.database.api.datasource.AccountRoutineLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.RoutineLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.SyncRoutineLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.AccountRoutineLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.RoutineLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncRoutineLocalEntity
import io.github.taetae98coding.diary.core.mapper.toLocal
import io.github.taetae98coding.diary.core.model.routine.RoutineDetail
import io.github.taetae98coding.diary.core.model.routine.RoutineRRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.mockk
import io.mockk.slot
import kotlin.time.Clock
import kotlin.uuid.Uuid

class AccountRoutineRepositoryImplTest : FunSpec() {
    private val clock = mockk<Clock>(relaxed = true)
    private val databaseTransactor = mockk<DatabaseTransactor>()
    private val routineLocalDataSource = mockk<RoutineLocalDataSource>(relaxUnitFun = true)
    private val accountRoutineLocalDataSource = mockk<AccountRoutineLocalDataSource>(relaxUnitFun = true)
    private val syncRoutineLocalDataSource = mockk<SyncRoutineLocalDataSource>(relaxUnitFun = true)
    private val repository = AccountRoutineRepositoryImpl(
        clock,
        databaseTransactor,
        routineLocalDataSource,
        accountRoutineLocalDataSource,
        syncRoutineLocalDataSource,
    )

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        beforeTest { clearAllMocks() }

        test("add") {
            val accountId = Uuid.random()
            val detail = fixtureMonkey.giveMeOne<RoutineDetail>()
            val rRules = fixtureMonkey.giveMeOne<List<RoutineRRule>>()
            val routineSlot = slot<RoutineLocalEntity>()
            val accountRoutineSlot = slot<AccountRoutineLocalEntity>()

            coEvery { databaseTransactor.writeTransaction(any<suspend () -> Unit>()) } coAnswers {
                firstArg<suspend () -> Unit>().invoke()
            }

            coEvery { routineLocalDataSource.upsert(capture(routineSlot)) } returns Unit
            coEvery { accountRoutineLocalDataSource.upsert(capture(accountRoutineSlot)) } returns Unit
            val syncRoutineSlot = slot<SyncRoutineLocalEntity>()
            coEvery { syncRoutineLocalDataSource.upsert(capture(syncRoutineSlot)) } returns Unit

            repository.add(accountId, detail, rRules)

            routineSlot.captured.detail shouldBe detail.toLocal()
            routineSlot.captured.rRules shouldBe rRules.map(RoutineRRule::toLocal)
            accountRoutineSlot.captured.accountId shouldBe accountId
            accountRoutineSlot.captured.routineId shouldBe routineSlot.captured.id
            syncRoutineSlot.captured.routineId shouldBe routineSlot.captured.id
        }

        test("add - transaction 내에서 순서대로 호출한다") {
            val accountId = Uuid.random()
            val detail = fixtureMonkey.giveMeOne<RoutineDetail>()
            val rRules = fixtureMonkey.giveMeOne<List<RoutineRRule>>()

            coEvery { databaseTransactor.writeTransaction(any<suspend () -> Unit>()) } coAnswers {
                firstArg<suspend () -> Unit>().invoke()
            }

            repository.add(accountId, detail, rRules)

            coVerifyOrder {
                routineLocalDataSource.upsert(any<RoutineLocalEntity>())
                accountRoutineLocalDataSource.upsert(any<AccountRoutineLocalEntity>())
                syncRoutineLocalDataSource.upsert(any<SyncRoutineLocalEntity>())
            }
        }
    }
}
