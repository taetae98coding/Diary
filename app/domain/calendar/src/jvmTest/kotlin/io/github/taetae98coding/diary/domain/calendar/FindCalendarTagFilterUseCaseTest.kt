package io.github.taetae98coding.diary.domain.calendar

import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.domain.calendar.usecase.FindCalendarSelectedTagFilterUseCase
import io.github.taetae98coding.diary.domain.calendar.usecase.FindCalendarTagFilterUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.PageTagUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.inspectors.shouldForAll
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeSortedBy
import io.kotest.matchers.collections.shouldBeUnique
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class FindCalendarTagFilterUseCaseTest : BehaviorSpec() {
	private val pageTagUseCase = mockk<PageTagUseCase>()
	private val findCalendarSelectedTagFilterUseCase = mockk<FindCalendarSelectedTagFilterUseCase>()
	private val useCase = FindCalendarTagFilterUseCase(
		pageTagUseCase = pageTagUseCase,
		findCalendarSelectedTagFilterUseCase = findCalendarSelectedTagFilterUseCase,
	)

	init {
		Given("has selected tag(id < 10)") {
			every { pageTagUseCase() } returns flowOf(Result.success(List(20, ::createTag)))
			every { findCalendarSelectedTagFilterUseCase() } returns flowOf(Result.success(List(10, ::createTag)))

			When("call useCase") {
				val result = useCase().first()

				Then("result is success") {
					result.shouldBeSuccess()
				}

				Then("distinct by id") {
					result.shouldBeSuccess().shouldBeUnique(
						Comparator { o1, o2 -> compareValues(o1.tag.id, o2.tag.id) },
					)
				}

				Then("sorted by title") {
					result.shouldBeSuccess().shouldBeSortedBy { it.tag.detail.title }
				}

				Then("id < 10 isSelected") {
					result.shouldBeSuccess().shouldForAll {
						if (it.tag.id.toInt() < 10) {
							it.isSelected.shouldBeTrue()
						} else {
							it.isSelected.shouldBeFalse()
						}
					}
				}
			}
		}
	}

	fun createTag(index: Int): Tag = mockk {
		every { id } returns index.toString()
		every { detail } returns mockk {
			every { title } returns "title"
		}
	}
}
