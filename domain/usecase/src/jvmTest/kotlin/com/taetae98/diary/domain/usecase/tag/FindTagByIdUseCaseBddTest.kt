package com.taetae98.diary.domain.usecase.tag

import com.taetae98.diary.domain.entity.tag.TagId
import com.taetae98.diary.domain.repository.TagRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class FindTagByIdUseCaseBddTest : BehaviorSpec({
    val tagRepository = mockk<TagRepository>()
    val useCase = FindTagByIdUseCase(tagRepository)

    Given("유효하지 않은 TagId가 주어질 때") {
        val tagId = TagId("").also { it.isInvalid().shouldBeTrue() }

        When("UseCase를 호출하면") {
            val result = useCase(tagId).first()

            Then("무시하기 때문에 성공한다.") {
                result.shouldBeSuccess()
            }

            Then("무시하기 때문에 find 함수가 호출되지 않는다.") {
                verify(exactly = 0) { tagRepository.find(any()) }
            }
        }
    }

    Given("유효한 TagId가 주어질 때") {
        val tagId = TagId("valid").also { it.isInvalid().shouldBeFalse() }

        every { tagRepository.find(tagId.value) } returns flowOf(null)

        When("UseCase를 호출하면") {
            val result = useCase(tagId).first()

            Then("성공한다.") {
                result.shouldBeSuccess()
            }

            Then("find 함수가 1회 호출된다.") {
                verify(exactly = 1) { tagRepository.find(tagId.value) }
            }
        }
    }
})
