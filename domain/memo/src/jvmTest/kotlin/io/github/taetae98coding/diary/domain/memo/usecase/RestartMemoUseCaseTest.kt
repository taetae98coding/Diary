package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.core.model.sync.SyncType
import io.github.taetae98coding.diary.domain.memo.repository.AccountMemoRepository
import io.github.taetae98coding.diary.domain.sync.usecase.RequestSyncUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlin.uuid.Uuid

class RestartMemoUseCaseTest : BehaviorSpec() {
    private val accountMemoRepository = mockk<AccountMemoRepository>()
    private val requestSyncUseCase = mockk<RequestSyncUseCase>(relaxed = true)
    private val useCase = RestartMemoUseCase(requestSyncUseCase, accountMemoRepository)

    init {
        Given("유효한 memoId") {
            clearAllMocks()
            val memoId = Uuid.random()

            coEvery { accountMemoRepository.updateFinish(memoId = memoId, isFinished = false) } returns Unit

            When("RestartMemoUseCase를 호출하면") {
                val result = useCase(memoId)

                Then("성공한다") {
                    result.shouldBeSuccess()
                }

                Then("repository의 updateFinish를 isFinished=false로 호출한다") {
                    coVerify(exactly = 1) { accountMemoRepository.updateFinish(memoId = memoId, isFinished = false) }
                }

                Then("RequestSyncUseCase를 호출한다") {
                    coVerify(exactly = 1) { requestSyncUseCase(SyncType.Background) }
                }
            }
        }
    }
}
