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

class DeleteRoutineUseCaseTest : BehaviorSpec() {
    private val requestSyncUseCase = mockk<RequestSyncUseCase>(relaxed = true)
    private val accountRoutineRepository = mockk<AccountRoutineRepository>()
    private val useCase = DeleteRoutineUseCase(requestSyncUseCase, accountRoutineRepository)

    init {
        Given("유효한 routineId") {
            clearAllMocks()
            val routineId = Uuid.random()

            coEvery { accountRoutineRepository.updateDelete(routineId = routineId, isDeleted = true) } returns Unit

            When("DeleteRoutineUseCase를 호출하면") {
                val result = useCase(routineId)

                Then("성공한다") {
                    result.shouldBeSuccess()
                }

                Then("repository의 updateDelete를 호출한다") {
                    coVerify(exactly = 1) { accountRoutineRepository.updateDelete(routineId = routineId, isDeleted = true) }
                }

                Then("RequestSyncUseCase를 호출한다") {
                    coVerify(exactly = 1) { requestSyncUseCase(SyncType.Background) }
                }
            }
        }
    }
}
