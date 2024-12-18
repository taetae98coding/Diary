package io.github.taetae98coding.diary.domain.memo

import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import io.github.taetae98coding.diary.domain.memo.usecase.FindMemoUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class FindMemoUseCaseTest : BehaviorSpec() {
	private val memoRepository = mockk<MemoRepository>()
	private val useCase = FindMemoUseCase(
		memoRepository = memoRepository,
	)

	init {
		Given("memoId is null") {
			val memoId = null

			When("call useCase") {
				val result = useCase(memoId).first()

				Then("result is null") {
					result
						.shouldBeSuccess()
						.shouldBeNull()
				}

				Then("do nothing") {
					verify { memoRepository wasNot Called }
				}
			}
		}

		Given("memoId is not null") {
			val memoId = "id"

			And("memo is deleted") {
				every { memoRepository.getById(any()) } returns flowOf(
					mockk { every { isDelete } returns true },
				)

				When("call useCase") {
					val result = useCase(memoId).first()

					Then("result is null") {
						result
							.shouldBeSuccess()
							.shouldBeNull()
					}
				}
			}

			And("memo is not deleted") {
				every { memoRepository.getById(any()) } returns flowOf(
					mockk { every { isDelete } returns false },
				)

				When("call useCase") {
					val result = useCase(memoId).first()

					Then("result is not null") {
						result
							.shouldBeSuccess()
							.shouldNotBeNull()
					}
				}
			}
		}
	}
}
