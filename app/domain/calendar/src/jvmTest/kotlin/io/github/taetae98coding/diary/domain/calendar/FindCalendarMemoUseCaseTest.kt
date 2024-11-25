package io.github.taetae98coding.diary.domain.calendar

import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.core.model.tag.TagDetail
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.calendar.usecase.FindCalendarMemoUseCase
import io.github.taetae98coding.diary.domain.calendar.usecase.FindCalendarSelectedTagFilterUseCase
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import io.github.taetae98coding.diary.domain.tag.repository.TagRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

class FindCalendarMemoUseCaseTest : BehaviorSpec() {
	init {
		val getAccountUseCase = mockk<GetAccountUseCase>()
		val findCalendarSelectedTagFilterUseCase = mockk<FindCalendarSelectedTagFilterUseCase>()
		val memoRepository = mockk<MemoRepository>(relaxed = true, relaxUnitFun = true)
		val tagRepository = mockk<TagRepository>(relaxed = true, relaxUnitFun = true)

		val useCase = FindCalendarMemoUseCase(
			getAccountUseCase = getAccountUseCase,
			findCalendarSelectedTagFilterUseCase = findCalendarSelectedTagFilterUseCase,
			memoRepository = memoRepository,
			tagRepository = tagRepository,
		)

		Given("has account") {
			val accountUid = "uid"
			val account = mockk<Account>(relaxed = true, relaxUnitFun = true) {
				every { uid } returns accountUid
			}
			every { getAccountUseCase() } returns flowOf(Result.success(account))

			And("has selected tag filter") {
				val selectedTagFilter = listOf(createTag())

				every { findCalendarSelectedTagFilterUseCase() } answers {
					flowOf(Result.success(selectedTagFilter))
				}

				When("call usecase") {
					val dateRange = LocalDate(2000, 1, 1)..LocalDate(2000, 1, 31)
					val memoList = List(2) { createMemo(it.toString(), if (it == 0) "0" else null) }
					val tagList = listOf(createTag())

					every { memoRepository.findByDateRange(any(), any(), any()) } returns flowOf(memoList)
					every { tagRepository.findByIds(any()) } returns flowOf(tagList)

					val result = useCase(dateRange).first()

					Then("result is success") {
						result.shouldBeSuccess()
					}

					Then("memo color changed") {
						val resultList = result.getOrThrow()
						val tag = tagList.first()

						resultList.first().detail.color shouldBe tag.detail.color
						resultList.last().detail.color shouldNotBe tag.detail.color
					}

					Then("do find") {
						verify {
							memoRepository.findByDateRange(accountUid, dateRange, selectedTagFilter.map { it.id }.toSet())
							tagRepository.findByIds(any())
						}
					}
				}
			}
		}
	}

	private fun createTag(): Tag = Tag(
		id = "0",
		detail = TagDetail(
			title = "title",
			description = "description",
			color = 100,
		),
		owner = null,
		isFinish = false,
		isDelete = false,
		updateAt = Instant.DISTANT_PAST,
	)

	private fun createMemo(id: String, primaryTag: String?): Memo = Memo(
		id = id,
		detail = MemoDetail(
			title = "title",
			description = "description",
			start = null,
			endInclusive = null,
			color = 0,
		),
		primaryTag = primaryTag,
		owner = null,
		isFinish = false,
		isDelete = false,
		updateAt = Instant.DISTANT_PAST,
	)
}
