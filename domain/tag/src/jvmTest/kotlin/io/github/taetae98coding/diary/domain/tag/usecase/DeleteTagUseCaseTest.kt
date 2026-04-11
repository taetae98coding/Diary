package io.github.taetae98coding.diary.domain.tag.usecase

import io.github.taetae98coding.diary.core.model.sync.SyncType
import io.github.taetae98coding.diary.domain.sync.usecase.RequestSyncUseCase
import io.github.taetae98coding.diary.domain.tag.repository.AccountTagRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlin.uuid.Uuid

class DeleteTagUseCaseTest : BehaviorSpec() {
    private val accountTagRepository = mockk<AccountTagRepository>()
    private val requestSyncUseCase = mockk<RequestSyncUseCase>(relaxed = true)
    private val useCase = DeleteTagUseCase(requestSyncUseCase, accountTagRepository)

    init {
        Given("유효한 tagId") {
            clearAllMocks()
            val tagId = Uuid.random()

            coEvery { accountTagRepository.updateDelete(tagId = tagId, isDeleted = true) } returns Unit

            When("DeleteTagUseCase를 호출하면") {
                val result = useCase(tagId)

                Then("성공한다") {
                    result.shouldBeSuccess()
                }

                Then("repository의 updateDelete를 호출한다") {
                    coVerify(exactly = 1) { accountTagRepository.updateDelete(tagId = tagId, isDeleted = true) }
                }

                Then("RequestSyncUseCase를 호출한다") {
                    coVerify(exactly = 1) { requestSyncUseCase(SyncType.Background) }
                }
            }
        }
    }
}
