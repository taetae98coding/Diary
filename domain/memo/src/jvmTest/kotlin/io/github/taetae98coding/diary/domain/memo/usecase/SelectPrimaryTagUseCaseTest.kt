package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.domain.memo.repository.AccountMemoTagRepository
import io.github.taetae98coding.diary.domain.sync.usecase.RequestSyncUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.mockk
import kotlin.uuid.Uuid

class SelectPrimaryTagUseCaseTest : BehaviorSpec() {
    private val accountMemoTagRepository = mockk<AccountMemoTagRepository>()
    private val requestSyncUseCase = mockk<RequestSyncUseCase>(relaxed = true)
    private val useCase = SelectPrimaryTagUseCase(accountMemoTagRepository, requestSyncUseCase)

    init {
        Given("유효한 memoId와 tagId") {
            clearAllMocks()
            val memoId = Uuid.random()
            val tagId = Uuid.random()

            coEvery { accountMemoTagRepository.updatePrimaryTag(memoId, tagId) } returns Unit

            When("SelectPrimaryTagUseCase를 호출하면") {
                val result = useCase(memoId, tagId)

                Then("성공한다") {
                    result.shouldBeSuccess()
                }

                Then("repository의 updatePrimaryTag를 호출한다") {
                    coVerify(exactly = 1) { accountMemoTagRepository.updatePrimaryTag(memoId, tagId) }
                }

                Then("updatePrimaryTag 후 RequestSyncUseCase를 호출한다") {
                    coVerifyOrder {
                        accountMemoTagRepository.updatePrimaryTag(memoId, tagId)
                        requestSyncUseCase()
                    }
                }
            }
        }

        Given("repository에서 예외가 발생하는 경우") {
            clearAllMocks()
            val memoId = Uuid.random()
            val tagId = Uuid.random()

            coEvery { accountMemoTagRepository.updatePrimaryTag(memoId, tagId) } throws RuntimeException()

            When("SelectPrimaryTagUseCase를 호출하면") {
                val result = useCase(memoId, tagId)

                Then("실패한다") {
                    result.shouldBeFailure<RuntimeException>()
                }

                Then("RequestSyncUseCase를 호출하지 않는다") {
                    coVerify(exactly = 0) { requestSyncUseCase() }
                }
            }
        }
    }
}
