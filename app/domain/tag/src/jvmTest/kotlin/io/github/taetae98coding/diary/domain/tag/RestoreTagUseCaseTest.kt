package io.github.taetae98coding.diary.domain.tag

import io.github.taetae98coding.diary.domain.backup.usecase.PushTagBackupQueueUseCase
import io.github.taetae98coding.diary.domain.tag.repository.TagRepository
import io.github.taetae98coding.diary.domain.tag.usecase.RestoreTagUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.Called
import io.mockk.clearAllMocks
import io.mockk.coVerifyOrder
import io.mockk.mockk
import io.mockk.verify

class RestoreTagUseCaseTest : BehaviorSpec() {
	private val pushTagBackupQueueUseCase = mockk<PushTagBackupQueueUseCase>(relaxed = true, relaxUnitFun = true)
	private val tagRepository = mockk<TagRepository>(relaxed = true, relaxUnitFun = true)
	private val useCase = RestoreTagUseCase(
		pushTagBackupQueueUseCase = pushTagBackupQueueUseCase,
		repository = tagRepository,
	)

	init {
		Given("tag id is not null") {
			val tagId = "tagId"

			When("call useCase") {
				val result = useCase(tagId)

				Then("result is success") {
					result.shouldBeSuccess()
				}

				Then("update finish and backup") {
					coVerifyOrder {
						tagRepository.updateDelete(tagId, false)
						pushTagBackupQueueUseCase(tagId)
					}
				}

				clearAllMocks()
			}
		}
		Given("tag id is null") {
			val tagId = null

			When("call useCase") {
				val result = useCase(tagId)

				Then("result is success") {
					result.shouldBeSuccess()
				}

				Then("do nothing") {
					verify {
						tagRepository wasNot Called
						pushTagBackupQueueUseCase wasNot Called
					}
				}

				clearAllMocks()
			}
		}
	}
}
