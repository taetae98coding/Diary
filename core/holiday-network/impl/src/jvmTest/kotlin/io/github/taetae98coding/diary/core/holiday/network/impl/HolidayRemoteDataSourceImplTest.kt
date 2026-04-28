package io.github.taetae98coding.diary.core.holiday.network.impl

import io.github.taetae98coding.diary.core.holiday.network.api.datasource.HolidayRemoteDataSource
import io.github.taetae98coding.diary.core.holiday.network.impl.di.HolidayHttpEngine
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.datetime.LocalDate
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import org.koin.plugin.module.dsl.koinApplication

class HolidayRemoteDataSourceImplTest : FunSpec() {
    init {
        afterTest { stopKoin() }

        test("get 2025") {
            val responseBody = this::class.java.classLoader
                .getResource("holiday/2025.json")!!
                .readText()

            val engine = MockEngine {
                respond(
                    content = responseBody,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }

            val koinApp = koinApplication<HolidayTestKoinApplication> {
                modules(
                    module {
                        single<HttpClientEngine>(qualifier = qualifier<HolidayHttpEngine>()) { engine }
                    },
                )
            }

            val dataSource = koinApp.koin.get<HolidayRemoteDataSource>()
            val result = dataSource.get(2025)

            result shouldHaveSize 3

            result[0].name shouldBe "신정"
            result[0].isHoliday shouldBe true
            result[0].start shouldBe LocalDate(2025, 1, 1)
            result[0].endInclusive shouldBe LocalDate(2025, 1, 1)

            result[1].name shouldBe "소한"
            result[1].isHoliday shouldBe false

            result[2].name shouldBe "설날"
            result[2].isHoliday shouldBe true
            result[2].start shouldBe LocalDate(2025, 1, 28)
            result[2].endInclusive shouldBe LocalDate(2025, 1, 30)
        }
    }
}
