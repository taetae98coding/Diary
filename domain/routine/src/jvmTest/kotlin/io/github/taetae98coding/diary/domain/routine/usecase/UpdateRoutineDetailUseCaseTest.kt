package io.github.taetae98coding.diary.domain.routine.usecase

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import io.github.taetae98coding.diary.core.model.routine.RoutineDetail
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

class UpdateRoutineDetailUseCaseTest : BehaviorSpec() {
    private val requestSyncUseCase = mockk<RequestSyncUseCase>(relaxed = true)
    private val accountRoutineRepository = mockk<AccountRoutineRepository>()
    private val useCase = UpdateRoutineDetailUseCase(requestSyncUseCase, accountRoutineRepository)

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        Given("유효한 routineId와 detail") {
            clearAllMocks()
            val routineId = Uuid.random()
            val detail = fixtureMonkey.giveMeOne<RoutineDetail>()

            coEvery { accountRoutineRepository.updateDetail(routineId = routineId, detail = detail) } returns Unit

            When("UpdateRoutineDetailUseCase를 호출하면") {
                val result = useCase(routineId, detail)

                Then("성공한다") {
                    result.shouldBeSuccess()
                }

                Then("repository의 updateDetail을 호출한다") {
                    coVerify(exactly = 1) { accountRoutineRepository.updateDetail(routineId = routineId, detail = detail) }
                }

                Then("RequestSyncUseCase를 호출한다") {
                    coVerify(exactly = 1) { requestSyncUseCase(SyncType.Background) }
                }
            }
        }
    }
}
