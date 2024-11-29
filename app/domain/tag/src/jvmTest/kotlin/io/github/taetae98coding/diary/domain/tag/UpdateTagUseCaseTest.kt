package io.github.taetae98coding.diary.domain.tag

import io.github.taetae98coding.diary.core.model.tag.TagDetail
import io.github.taetae98coding.diary.domain.backup.usecase.PushTagBackupQueueUseCase
import io.github.taetae98coding.diary.domain.tag.repository.TagRepository
import io.github.taetae98coding.diary.domain.tag.usecase.UpdateTagUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.Called
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf

class UpdateTagUseCaseTest : BehaviorSpec() {
	private val pushTagBackupQueueUseCase = mockk<PushTagBackupQueueUseCase>(relaxed = true, relaxUnitFun = true)
	private val tagRepository = mockk<TagRepository>(relaxed = true, relaxUnitFun = true)
	private val useCase = UpdateTagUseCase(
		pushTagBackupQueueUseCase = pushTagBackupQueueUseCase,
		repository = tagRepository,
	)

	init {
		Given("title not blank detail") {
			val detail = TagDetail(
				title = "title",
				description = "description",
				color = 0,
			)

			And("origin detail and new detail different") {
				every { tagRepository.getById(any()) } returns flowOf(mockk(relaxed = true, relaxUnitFun = true))

				When("call useCase") {
					val result = useCase("memoId", detail)

					Then("result is success") {
						result.shouldBeSuccess()
					}

					Then("do update") {
						coVerify {
							tagRepository.update(any(), any())
							pushTagBackupQueueUseCase(any())
						}
					}

					clearAllMocks()
				}
			}

			And("origin detail and new detail same") {
				every { tagRepository.getById(any()) } returns flowOf(
					mockk {
						every { this@mockk.detail } returns detail
					},
				)

				When("call useCase") {
					val result = useCase("memoId", detail)

					Then("result is success") {
						result.shouldBeSuccess()
					}

					Then("do not update") {
						coVerify(exactly = 0) {
							tagRepository.update(any(), any())
							pushTagBackupQueueUseCase(any())
						}
					}

					clearAllMocks()
				}
			}
		}

		Given("title blank detail") {
			val detail = TagDetail(
				title = "",
				description = "description",
				color = 0,
			)

			When("call useCase") {
				every { tagRepository.getById(any()) } returns flowOf(
					mockk {
						every { this@mockk.detail } returns mockk {
							every { title } returns "title"
						}
					},
				)

				val result = useCase("memoId", detail)

				Then("result is success") {
					result.shouldBeSuccess()
				}

				Then("detail title set to origin title") {
					coVerify { tagRepository.update(any(), detail.copy(title = "title")) }
				}

				clearAllMocks()
			}
		}

		Given("tag id is null") {
			val tagId = null

			When("call useCase") {
				val result = useCase(tagId, mockk())

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
