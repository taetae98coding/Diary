package io.github.taetae98coding.diary.domain.routine.usecase

import io.github.taetae98coding.diary.core.model.sync.SyncType
import io.github.taetae98coding.diary.domain.routine.repository.AccountRoutineRepository
import io.github.taetae98coding.diary.domain.sync.usecase.RequestSyncUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlin.uuid.Uuid

class FinishRoutineUseCaseTest : BehaviorSpec() {
    private val requestSyncUseCase = mockk<RequestSyncUseCase>(relaxed = true)
    private val accountRoutineRepository = mockk<AccountRoutineRepository>()
    private val useCase = FinishRoutineUseCase(requestSyncUseCase, accountRoutineRepository)

    init {
        Given("유효한 routineId") {
            clearAllMocks()
            val routineId = Uuid.random()

            coEvery { accountRoutineRepository.updateFinish(routineId = routineId, isFinished = true) } returns Unit

            When("FinishRoutineUseCase를 호출하면") {
                val result = useCase(routineId)

                Then("성공한다") {
                    result.shouldBeSuccess()
                }

                Then("repository의 updateFinish를 호출한다") {
                    coVerify(exactly = 1) { accountRoutineRepository.updateFinish(routineId = routineId, isFinished = true) }
                }

                Then("RequestSyncUseCase를 호출한다") {
                    coVerify(exactly = 1) { requestSyncUseCase(SyncType.Background) }
                }
            }
        }
    }
}
