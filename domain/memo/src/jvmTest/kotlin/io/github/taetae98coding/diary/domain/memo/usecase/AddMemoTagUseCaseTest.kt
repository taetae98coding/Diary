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

class AddMemoTagUseCaseTest : BehaviorSpec() {
    private val accountMemoTagRepository = mockk<AccountMemoTagRepository>()
    private val requestSyncUseCase = mockk<RequestSyncUseCase>(relaxed = true)
    private val useCase = AddMemoTagUseCase(accountMemoTagRepository, requestSyncUseCase)

    init {
        Given("유효한 memoId와 tagId") {
            clearAllMocks()
            val memoId = Uuid.random()
            val tagId = Uuid.random()

            coEvery { accountMemoTagRepository.upsertMemoTag(memoId, tagId, isMemoTag = true) } returns Unit

            When("AddMemoTagUseCase를 호출하면") {
                val result = useCase(memoId, tagId)

                Then("성공한다") {
                    result.shouldBeSuccess()
                }

                Then("repository의 upsertMemoTag를 isMemoTag=true로 호출한다") {
                    coVerify(exactly = 1) { accountMemoTagRepository.upsertMemoTag(memoId, tagId, isMemoTag = true) }
                }

                Then("upsertMemoTag 후 RequestSyncUseCase를 호출한다") {
                    coVerifyOrder {
                        accountMemoTagRepository.upsertMemoTag(memoId, tagId, isMemoTag = true)
                        requestSyncUseCase()
                    }
                }
            }
        }

        Given("repository에서 예외가 발생하는 경우") {
            clearAllMocks()
            val memoId = Uuid.random()
            val tagId = Uuid.random()

            coEvery { accountMemoTagRepository.upsertMemoTag(memoId, tagId, isMemoTag = true) } throws RuntimeException()

            When("AddMemoTagUseCase를 호출하면") {
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
