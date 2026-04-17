package io.github.taetae98coding.diary.core.mapper

import io.github.taetae98coding.diary.core.database.api.entity.RRuleDiaryByDayLocalEntity
import io.github.taetae98coding.diary.core.network.api.entity.RRuleDiaryByDayRemoteEntity
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.DayOfWeek

class RRuleDiaryByDayRemoteMapperTest : FunSpec() {
    init {
        test("Local to Remote") {
            val entity = RRuleDiaryByDayLocalEntity(
                days = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
                ordinal = 2,
            )

            val result = entity.toRemote()

            result.days shouldBe listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)
            result.ordinal shouldBe 2
        }

        test("Local to Remote — default") {
            val result = RRuleDiaryByDayLocalEntity().toRemote()

            result.days shouldBe emptyList()
            result.ordinal shouldBe null
        }

        test("Remote to Local") {
            val entity = RRuleDiaryByDayRemoteEntity(
                days = listOf(DayOfWeek.TUESDAY),
                ordinal = -1,
            )

            val result = entity.toLocal()

            result.days shouldBe listOf(DayOfWeek.TUESDAY)
            result.ordinal shouldBe -1
        }

        test("Remote to Local — default") {
            val result = RRuleDiaryByDayRemoteEntity().toLocal()

            result.days shouldBe emptyList()
            result.ordinal shouldBe null
        }
    }
}
