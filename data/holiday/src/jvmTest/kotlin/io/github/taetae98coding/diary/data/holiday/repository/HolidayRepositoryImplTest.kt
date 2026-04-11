package io.github.taetae98coding.diary.data.holiday.repository

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import io.github.taetae98coding.diary.core.holiday.database.api.HolidayLocalDataSource
import io.github.taetae98coding.diary.core.holiday.database.api.entity.HolidayLocalEntity
import io.github.taetae98coding.diary.core.holiday.network.api.datasource.HolidayRemoteDataSource
import io.github.taetae98coding.diary.core.holiday.network.api.entity.HolidayRemoteEntity
import io.github.taetae98coding.diary.core.mapper.toLocal
import io.kotest.core.spec.style.FunSpec
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.mockk

class HolidayRepositoryImplTest : FunSpec() {
    private lateinit var holidayRemoteDataSource: HolidayRemoteDataSource
    private lateinit var holidayLocalDataSource: HolidayLocalDataSource
    private lateinit var repository: HolidayRepositoryImpl

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        beforeTest {
            clearAllMocks()
            holidayRemoteDataSource = mockk()
            holidayLocalDataSource = mockk(relaxUnitFun = true)
            repository = HolidayRepositoryImpl(
                holidayRemoteDataSource,
                holidayLocalDataSource,
            )
        }

        test("fetch - remote에서 가져온 데이터를 local에 저장한다") {
            val year = 2026
            val remoteHolidays = fixtureMonkey.giveMeOne<List<HolidayRemoteEntity>>()

            coEvery { holidayRemoteDataSource.get(year) } returns remoteHolidays

            repository.fetch(year)

            coVerifyOrder {
                holidayRemoteDataSource.get(year)
                holidayLocalDataSource.upsert(year, remoteHolidays.map(HolidayRemoteEntity::toLocal))
            }
        }

        test("fetch - 같은 year를 두 번 호출하면 두 번째는 remote를 호출하지 않는다") {
            val year = 2026
            val remoteHolidays = fixtureMonkey.giveMeOne<List<HolidayRemoteEntity>>()

            coEvery { holidayRemoteDataSource.get(year) } returns remoteHolidays

            repository.fetch(year)
            repository.fetch(year)

            coVerify(exactly = 1) { holidayRemoteDataSource.get(year) }
            coVerify(exactly = 1) { holidayLocalDataSource.upsert(year, any<List<HolidayLocalEntity>>()) }
        }

        test("fetch - 다른 year는 각각 remote를 호출한다") {
            val year1 = 2025
            val year2 = 2026
            val remote1 = fixtureMonkey.giveMeOne<List<HolidayRemoteEntity>>()
            val remote2 = fixtureMonkey.giveMeOne<List<HolidayRemoteEntity>>()

            coEvery { holidayRemoteDataSource.get(year1) } returns remote1
            coEvery { holidayRemoteDataSource.get(year2) } returns remote2

            repository.fetch(year1)
            repository.fetch(year2)

            coVerify(exactly = 1) { holidayRemoteDataSource.get(year1) }
            coVerify(exactly = 1) { holidayRemoteDataSource.get(year2) }
        }
    }
}
