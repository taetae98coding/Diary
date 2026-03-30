package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.core.model.sync.SyncType
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

class RemoveMemoTagUseCaseTest : BehaviorSpec() {
    private val accountMemoTagRepository = mockk<AccountMemoTagRepository>()
    private val requestSyncUseCase = mockk<RequestSyncUseCase>(relaxed = true)
    private val useCase = RemoveMemoTagUseCase(accountMemoTagRepository, requestSyncUseCase)

    init {
        Given("유효한 memoId와 tagId") {
            clearAllMocks()
            val memoId = Uuid.random()
            val tagId = Uuid.random()

            coEvery { accountMemoTagRepository.upsertMemoTag(memoId, tagId, isMemoTag = false) } returns Unit

            When("RemoveMemoTagUseCase를 호출하면") {
                val result = useCase(memoId, tagId)

                Then("성공한다") {
                    result.shouldBeSuccess()
                }

                Then("repository의 upsertMemoTag를 isMemoTag=false로 호출한다") {
                    coVerify(exactly = 1) { accountMemoTagRepository.upsertMemoTag(memoId, tagId, isMemoTag = false) }
                }

                Then("upsertMemoTag 후 RequestSyncUseCase를 호출한다") {
                    coVerifyOrder {
                        accountMemoTagRepository.upsertMemoTag(memoId, tagId, isMemoTag = false)
                        requestSyncUseCase(SyncType.Background)
                    }
                }
            }
        }

        Given("repository에서 예외가 발생하는 경우") {
            clearAllMocks()
            val memoId = Uuid.random()
            val tagId = Uuid.random()

            coEvery { accountMemoTagRepository.upsertMemoTag(memoId, tagId, isMemoTag = false) } throws RuntimeException()

            When("RemoveMemoTagUseCase를 호출하면") {
                val result = useCase(memoId, tagId)

                Then("실패한다") {
                    result.shouldBeFailure<RuntimeException>()
                }

                Then("RequestSyncUseCase를 호출하지 않는다") {
                    coVerify(exactly = 0) { requestSyncUseCase(SyncType.Background) }
                }
            }
        }
    }
}
