package io.github.taetae98coding.diary.domain.calendar

import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.calendar.usecase.FindCalendarSelectedTagFilterUseCase
import io.github.taetae98coding.diary.domain.calendar.usecase.PageCalendarMemoUseCase
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import io.github.taetae98coding.diary.domain.tag.repository.TagRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.inspectors.shouldForAll
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

class PageCalendarMemoUseCaseTest : BehaviorSpec() {
	private val getAccountUseCase = mockk<GetAccountUseCase>()
	private val findCalendarSelectedTagFilterUseCase = mockk<FindCalendarSelectedTagFilterUseCase>()
	private val memoRepository = mockk<MemoRepository>()
	private val tagRepository = mockk<TagRepository>()
	private val useCase = PageCalendarMemoUseCase(
		getAccountUseCase = getAccountUseCase,
		findCalendarSelectedTagFilterUseCase = findCalendarSelectedTagFilterUseCase,
		memoRepository = memoRepository,
		tagRepository = tagRepository,
	)

	init {
		Given("has account") {
			val accountUid = "uid"
			val account = mockk<Account> {
				every { uid } returns accountUid
			}

			every { getAccountUseCase() } returns flowOf(Result.success(account))

			And("has selected tag filter") {
				val selectedTagFilter = List(10, ::createTag)
				every { findCalendarSelectedTagFilterUseCase() } returns flowOf(Result.success(selectedTagFilter))

				And("has memo and tag") {
					val memo = List(10, ::createMemo)
					every { memoRepository.findByDateRange(any(), any(), any()) } returns flowOf(memo)

					every { tagRepository.getByIds(any()) } answers {
						flowOf(arg<Set<String>>(0).map { createTag(it.toInt()) })
					}

					When("call useCase") {
						val dateRange = LocalDate(2000, 1, 1)..LocalDate(2000, 1, 2)
						val result = useCase(dateRange).first()

						Then("result is success") {
							result.shouldBeSuccess()
						}

						Then("verify call pageByDateRange with uid, dateRange, selectedTagFilter") {
							val selectedTagFilterIds = selectedTagFilter.map { it.id }.toSet()
							verify { memoRepository.findByDateRange(accountUid, dateRange, selectedTagFilterIds) }
						}

						Then("verify call find tag with primaryTag ids") {
							val tagIds = memo.mapNotNull { it.primaryTag }.toSet()
							verify { tagRepository.getByIds(tagIds) }
						}

						Then("copy color if primaryTag is not null and primaryTag is not deleted") {
							result.shouldBeSuccess().shouldForAll {
								val primaryTag = it.primaryTag

								if (primaryTag.isNullOrBlank()) {
									it.detail.color shouldBe MEMO_COLOR
								} else {
									if (primaryTag.toInt().isDeleteTag()) {
										it.detail.color shouldBe MEMO_COLOR
									} else {
										it.detail.color shouldBe TAG_COLOR
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private fun createMemo(index: Int): Memo = Memo(
		id = index.toString(),
		detail = MemoDetail(
			title = "title",
			description = "description",
			start = null,
			endInclusive = null,
			color = MEMO_COLOR,
		),
		primaryTag = index.toString().takeIf { index % 2 == 0 },
		owner = null,
		isFinish = false,
		isDelete = false,
		updateAt = Instant.DISTANT_PAST,
	)

	private fun createTag(index: Int): Tag = mockk {
		every { id } returns index.toString()
		every { isDelete } returns index.isDeleteTag()
		every { detail } returns mockk {
			every { color } returns TAG_COLOR
		}
	}

	private fun Int.isDeleteTag(): Boolean = this % 3 == 0

	companion object {
		private const val MEMO_COLOR = 0
		private const val TAG_COLOR = 1
	}
}
