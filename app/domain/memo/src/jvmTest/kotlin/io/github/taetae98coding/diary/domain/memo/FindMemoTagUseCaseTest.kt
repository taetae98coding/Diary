package io.github.taetae98coding.diary.domain.memo

import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.domain.memo.repository.MemoTagRepository
import io.github.taetae98coding.diary.domain.memo.usecase.FindMemoTagUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.FindMemoUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.PageTagUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.inspectors.shouldForAll
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldBeSortedBy
import io.kotest.matchers.collections.shouldBeUnique
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.Called
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class FindMemoTagUseCaseTest : BehaviorSpec() {
	private val findMemoUseCase = mockk<FindMemoUseCase>()
	private val pageTagUseCase = mockk<PageTagUseCase> {
		every { this@mockk.invoke() } returns flowOf(Result.success(List(20, ::createTag).filterNot { it.isDelete }))
	}
	private val memoTagRepository = mockk<MemoTagRepository> {
		every { findTagByMemoId(any()) } returns flowOf(List(10, ::createTag))
	}
	private val useCase = FindMemoTagUseCase(
		findMemoUseCase = findMemoUseCase,
		pageTagUseCase = pageTagUseCase,
		repository = memoTagRepository,
	)

	init {
		Given("memoId is null") {
			val memoId = null

			When("call useCase") {
				val result = useCase(memoId).first()

				Then("result is success") {
					result.shouldBeSuccess()
				}

				Then("do nothing") {
					verify {
						memoTagRepository wasNot Called
						pageTagUseCase wasNot Called
						findMemoUseCase wasNot Called
					}
				}
			}
		}

		Given("memoId is not null") {
			val memoId = "id"

			And("memo is null") {
				every { findMemoUseCase(any()) } answers { flowOf(Result.success(null)) }

				When("call useCase") {
					val result = useCase(memoId).first()

					Then("result is success") {
						result.shouldBeSuccess()
					}

					Then("return empty list") {
						result.shouldBeSuccess().shouldBeEmpty()
					}

					clearAllMocks(answers = false)
				}
			}

			And("memo is not null") {
				every { findMemoUseCase(any()) } answers { flowOf(Result.success(createMemo(arg(0)))) }

				When("call useCase") {
					val result = useCase(memoId).first()

					Then("result is success") {
						result.shouldBeSuccess()
					}

					Then("has not deleted tag") {
						result
							.shouldBeSuccess()
							.shouldForAll { it.tag.isDelete.shouldBeFalse() }
					}

					Then("sorted by title") {
						result
							.shouldBeSuccess()
							.shouldBeSortedBy { it.tag.detail.title }
					}

					Then("distinct by id") {
						result
							.shouldBeSuccess()
							.shouldBeUnique(Comparator { o1, o2 -> compareValues(o1.tag.id, o2.tag.id) })
					}

					Then("isSelected id < 10") {
						result
							.shouldBeSuccess()
							.shouldForAll {
								if (it.tag.id.toInt() < 10) {
									it.isSelected.shouldBeTrue()
								} else {
									it.isSelected.shouldBeFalse()
								}
							}
					}

					Then("memo primaryTag isPrimary") {
						result
							.shouldBeSuccess()
							.shouldForAll {
								if (it.tag.id == "1") {
									it.isPrimary.shouldBeTrue()
								} else {
									it.isPrimary.shouldBeFalse()
								}
							}
					}

					clearAllMocks(answers = false)
				}
			}
		}
	}

	private fun createMemo(memoId: String): Memo = mockk(relaxed = true, relaxUnitFun = true) {
		every { id } returns memoId
		every { primaryTag } returns "1"
	}

	private fun createTag(index: Int): Tag = mockk(relaxed = true, relaxUnitFun = true) {
		every { id } returns index.toString()
		every { detail } returns mockk(relaxed = true, relaxUnitFun = true) {
			every { title } returns (10 - index).toString()
		}
		every { isDelete } returns (index % 2 == 0)
	}
}
