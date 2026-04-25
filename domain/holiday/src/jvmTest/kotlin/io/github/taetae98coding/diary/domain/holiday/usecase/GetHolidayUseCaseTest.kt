package io.github.taetae98coding.diary.domain.holiday.usecase

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.github.taetae98coding.diary.core.model.holiday.Holiday
import io.github.taetae98coding.diary.domain.holiday.repository.HolidayFilterRepository
import io.github.taetae98coding.diary.domain.holiday.repository.HolidayRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDate

class GetHolidayUseCaseTest : BehaviorSpec() {
    private val holidayRepository = mockk<HolidayRepository>()
    private val holidayFilterRepository = mockk<HolidayFilterRepository>()
    private val useCase = GetHolidayUseCase(holidayRepository, holidayFilterRepository)

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        Given("필터가 비어있을 때") {
            clearAllMocks()
            val year = 2026
            val holidays = listOf(
                fixtureMonkey.giveMeKotlinBuilder<Holiday>()
                    .set(Holiday::name, "신정")
                    .set(Holiday::localDateRange, LocalDate(2026, 1, 1)..LocalDate(2026, 1, 1))
                    .sample(),
                fixtureMonkey.giveMeKotlinBuilder<Holiday>()
                    .set(Holiday::name, "설날")
                    .set(Holiday::localDateRange, LocalDate(2026, 1, 28)..LocalDate(2026, 1, 30))
                    .sample(),
            )

            every { holidayRepository.get(year) } returns flowOf(holidays)
            every { holidayFilterRepository.get() } returns flowOf(emptySet())

            When("GetHolidayUseCase를 호출하면") {
                val result = useCase(year).first()

                Then("모든 공휴일을 반환한다") {
                    result.shouldBeSuccess(holidays)
                }

                Then("HolidayRepository를 호출한다") {
                    verify(exactly = 1) { holidayRepository.get(year) }
                }

                Then("HolidayFilterRepository를 호출한다") {
                    verify(exactly = 1) { holidayFilterRepository.get() }
                }
            }
        }

        Given("필터에 특정 공휴일 이름이 있을 때") {
            clearAllMocks()
            val year = 2026
            val sinjeong = fixtureMonkey.giveMeKotlinBuilder<Holiday>()
                .set(Holiday::name, "신정")
                .set(Holiday::isHoliday, false)
                .set(Holiday::localDateRange, LocalDate(2026, 1, 1)..LocalDate(2026, 1, 1))
                .sample()
            val seolnal = fixtureMonkey.giveMeKotlinBuilder<Holiday>()
                .set(Holiday::name, "설날")
                .set(Holiday::isHoliday, false)
                .set(Holiday::localDateRange, LocalDate(2026, 1, 28)..LocalDate(2026, 1, 30))
                .sample()
            val holidays = listOf(sinjeong, seolnal)
            val filter = setOf("신정")

            every { holidayRepository.get(year) } returns flowOf(holidays)
            every { holidayFilterRepository.get() } returns flowOf(filter)

            When("GetHolidayUseCase를 호출하면") {
                val result = useCase(year).first()

                Then("필터에 포함된 공휴일만 반환한다") {
                    result.shouldBeSuccess(listOf(sinjeong))
                }
            }
        }

        Given("필터가 있지만 isHoliday가 true인 공휴일이 있을 때") {
            clearAllMocks()
            val year = 2026
            val sinjeong = fixtureMonkey.giveMeKotlinBuilder<Holiday>()
                .set(Holiday::name, "신정")
                .set(Holiday::isHoliday, true)
                .set(Holiday::localDateRange, LocalDate(2026, 1, 1)..LocalDate(2026, 1, 1))
                .sample()
            val seolnal = fixtureMonkey.giveMeKotlinBuilder<Holiday>()
                .set(Holiday::name, "설날")
                .set(Holiday::isHoliday, false)
                .set(Holiday::localDateRange, LocalDate(2026, 1, 28)..LocalDate(2026, 1, 30))
                .sample()
            val holidays = listOf(sinjeong, seolnal)
            val filter = setOf("설날")

            every { holidayRepository.get(year) } returns flowOf(holidays)
            every { holidayFilterRepository.get() } returns flowOf(filter)

            When("GetHolidayUseCase를 호출하면") {
                val result = useCase(year).first()

                Then("isHoliday가 true인 공휴일과 필터에 포함된 공휴일을 모두 반환한다") {
                    result.shouldBeSuccess(listOf(sinjeong, seolnal))
                }
            }
        }

        Given("필터에 매칭되는 공휴일이 없을 때") {
            clearAllMocks()
            val year = 2026
            val holidays = listOf(
                fixtureMonkey.giveMeKotlinBuilder<Holiday>()
                    .set(Holiday::name, "신정")
                    .set(Holiday::isHoliday, false)
                    .set(Holiday::localDateRange, LocalDate(2026, 1, 1)..LocalDate(2026, 1, 1))
                    .sample(),
            )
            val filter = setOf("존재하지않는공휴일")

            every { holidayRepository.get(year) } returns flowOf(holidays)
            every { holidayFilterRepository.get() } returns flowOf(filter)

            When("GetHolidayUseCase를 호출하면") {
                val result = useCase(year).first()

                Then("빈 리스트를 반환한다") {
                    result.shouldBeSuccess(emptyList())
                }
            }
        }

        Given("HolidayRepository에서 예외가 발생할 때") {
            clearAllMocks()
            val year = 2026
            val exception = RuntimeException("error")

            every { holidayRepository.get(year) } throws exception
            every { holidayFilterRepository.get() } returns flowOf(emptySet())

            When("GetHolidayUseCase를 호출하면") {
                val result = useCase(year).first()

                Then("Result.failure를 반환한다") {
                    result.shouldBeFailure<RuntimeException>()
                }
            }
        }
    }
}
