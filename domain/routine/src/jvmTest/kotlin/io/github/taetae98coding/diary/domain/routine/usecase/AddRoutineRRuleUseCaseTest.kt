package io.github.taetae98coding.diary.domain.routine.usecase

import io.github.taetae98coding.diary.core.model.routine.RoutineRRule
import io.github.taetae98coding.diary.core.model.sync.SyncType
import io.github.taetae98coding.diary.domain.routine.repository.AccountRoutineRRuleRepository
import io.github.taetae98coding.diary.domain.sync.usecase.RequestSyncUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlin.uuid.Uuid
import kotlinx.datetime.DayOfWeek

class AddRoutineRRuleUseCaseTest : BehaviorSpec() {
    private val requestSyncUseCase = mockk<RequestSyncUseCase>(relaxed = true)
    private val accountRoutineRRuleRepository = mockk<AccountRoutineRRuleRepository>()
    private val useCase = AddRoutineRRuleUseCase(requestSyncUseCase, accountRoutineRRuleRepository)

    init {
        Given("유효한 routineId와 rRules") {
            clearAllMocks()
            val routineId = Uuid.random()
            val rRules = listOf(RoutineRRule.ByDay(dayOfWeek = DayOfWeek.MONDAY))

            coEvery { accountRoutineRRuleRepository.add(routineId = routineId, rRules = rRules) } returns Unit

            When("AddRoutineRRuleUseCase를 호출하면") {
                val result = useCase(routineId, rRules)

                Then("성공한다") {
                    result.shouldBeSuccess()
                }

                Then("repository의 add를 호출한다") {
                    coVerify(exactly = 1) { accountRoutineRRuleRepository.add(routineId = routineId, rRules = rRules) }
                }

                Then("RequestSyncUseCase를 호출한다") {
                    coVerify(exactly = 1) { requestSyncUseCase(SyncType.Background) }
                }
            }
        }
    }
}
