package io.github.taetae98coding.diary.domain.tag

import io.github.taetae98coding.diary.domain.tag.repository.TagMemoRepository
import io.github.taetae98coding.diary.domain.tag.usecase.PageTagMemoUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.Called
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class FindTagMemoUseCaseTest : BehaviorSpec() {
	private val tagMemoRepository = mockk<TagMemoRepository>(relaxed = true, relaxUnitFun = true)
	private val useCase = PageTagMemoUseCase(
		repository = tagMemoRepository,
	)

	init {
		Given("tag id is not null") {
			val tagId = "tagId"

			When("call useCase") {
				every { tagMemoRepository.pageMemoByTagId(any()) } returns flowOf(emptyList())

				val result = useCase(tagId).first()

				Then("result is success") {
					result.shouldBeSuccess()
				}

				Then("find memo by tag id") {
					verify {
						tagMemoRepository.pageMemoByTagId(tagId)
					}
				}

				clearAllMocks()
			}
		}

		Given("tag id is null") {
			val tagId = null

			When("call useCase") {
				val result = useCase(tagId).first()

				Then("result is empty") {
					result.shouldBeSuccess()
				}

				Then("do nothing") {
					verify {
						tagMemoRepository wasNot Called
					}
				}

				clearAllMocks()
			}
		}
	}
}
