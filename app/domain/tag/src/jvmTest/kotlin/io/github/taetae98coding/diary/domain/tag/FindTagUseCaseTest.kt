package io.github.taetae98coding.diary.domain.tag

import io.github.taetae98coding.diary.domain.tag.repository.TagRepository
import io.github.taetae98coding.diary.domain.tag.usecase.FindTagUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.Called
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class FindTagUseCaseTest : BehaviorSpec() {
	private val tagRepository = mockk<TagRepository>(relaxed = true, relaxUnitFun = true)
	private val useCase = FindTagUseCase(repository = tagRepository)

	init {
		Given("tag id is not null") {
			val tagId = "tagId"

			And("tag is deleted") {
				every { tagRepository.getById(any()) } returns flowOf(
					mockk {
						every { isDelete } returns true
					},
				)

				When("call useCase") {
					val result = useCase(tagId).first()

					Then("result is null") {
						result
							.shouldBeSuccess()
							.shouldBeNull()
					}

					clearAllMocks()
				}
			}

			And("tag is not deleted") {
				every { tagRepository.getById(any()) } returns flowOf(
					mockk {
						every { isDelete } returns false
					},
				)

				When("call useCase") {
					val result = useCase(tagId).first()

					Then("result is not null") {
						result
							.shouldBeSuccess()
							.shouldNotBeNull()
					}

					clearAllMocks()
				}
			}
		}
		Given("tag id is null") {
			val tagId = null

			When("call useCase") {
				val result = useCase(tagId).first()

				Then("result is success") {
					result.shouldBeSuccess()
				}

				Then("do nothing") {
					verify {
						tagRepository wasNot Called
					}
				}
			}
		}
	}
}
