package io.github.taetae98coding.diary.domain.holiday.usecase

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.github.taetae98coding.diary.core.model.holiday.Holiday
import io.github.taetae98coding.diary.domain.holiday.repository.HolidayRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDate

class GetGoldenHolidayUseCaseTest : BehaviorSpec() {
    init {
        val fixtureMonkey = FixtureMonkey.builder()
            .plugin(KotlinPlugin())
            .build()

        val holidayRepository = mockk<HolidayRepository>(relaxed = true)

        val useCase = GetGoldenHolidayUseCase(
            holidayRepository = holidayRepository,
        )

        val year = 2026

        Given("공휴일이 없을 때") {
            every { holidayRepository.get(year) } returns flowOf(emptyList())

            When("invoke를 호출하면") {
                val result = useCase(year, annualLeave = 0).first()

                Then("Result는 성공이고 빈 리스트를 반환한다") {
                    result.shouldBeSuccess()
                    result.getOrNull()!!.shouldBeEmpty()
                }

                Then("holidayRepository.get이 1회 호출된다") {
                    verify(exactly = 1) { holidayRepository.get(year) }
                }
            }
        }

        Given("공휴일이 주말과 연결될 때") {
            val holiday = fixtureMonkey.giveMeKotlinBuilder<Holiday>()
                .set(Holiday::isHoliday, true)
                .set(Holiday::localDateRange, LocalDate(2026, 1, 1)..LocalDate(2026, 1, 1))
                .sample()

            every { holidayRepository.get(year) } returns flowOf(listOf(holiday))

            When("annualLeave가 1일이면") {
                val result = useCase(year, annualLeave = 1).first()

                Then("Result는 성공이고 황금연휴를 반환한다") {
                    result.shouldBeSuccess()
                    result.getOrNull()!!.shouldNotBeEmpty()
                }
            }
        }

        Given("연속 공휴일이 있을 때") {
            val holidays = listOf(
                fixtureMonkey.giveMeKotlinBuilder<Holiday>()
                    .set(Holiday::isHoliday, true)
                    .set(Holiday::localDateRange, LocalDate(2026, 8, 15)..LocalDate(2026, 8, 15))
                    .sample(),
                fixtureMonkey.giveMeKotlinBuilder<Holiday>()
                    .set(Holiday::isHoliday, true)
                    .set(Holiday::localDateRange, LocalDate(2026, 8, 17)..LocalDate(2026, 8, 17))
                    .sample(),
            )

            every { holidayRepository.get(year) } returns flowOf(holidays)

            When("annualLeave가 0이면") {
                val result = useCase(year, annualLeave = 0).first()

                Then("Result는 성공이다") {
                    result.shouldBeSuccess()
                }

                Then("반환된 황금연휴의 dateRange에 공휴일이 포함된다") {
                    val goldenHolidays = result.getOrNull()!!
                    goldenHolidays.shouldNotBeEmpty()
                    goldenHolidays.all { it.holiday.isNotEmpty() } shouldBe true
                }
            }
        }
    }
}
