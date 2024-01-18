package com.taetae98.diary.domain.usecase.tag

import com.taetae98.diary.domain.entity.tag.Tag
import com.taetae98.diary.domain.exception.TitleEmptyException
import com.taetae98.diary.domain.repository.TagRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.every
import io.mockk.mockk

class UpsertTagUseCaseBddTest : BehaviorSpec({
    val tagRepository = mockk<TagRepository>(relaxed = true, relaxUnitFun = true)
    val useCase = UpsertTagUseCase(
        tagRepository = tagRepository
    )

    Given("Title이 Empty인 Tag가 주어졌을 때") {
        val tag = mockk<Tag> {
            every { title } returns ""
        }

        When("UseCase를 호출하면") {
            val result = useCase(tag)

            Then("TitleEmptyException 발생한다.") {
                result.shouldBeFailure<TitleEmptyException>()
            }
        }
    }

    Given("Title이 문자로 이루어진 Tag가 주어졌을 때") {
        val tag = mockk<Tag> {
            every { title } returns "asdf"
        }

        When("UseCase를 호출하면") {
            val result = useCase(tag)

            Then("통과한다.") {
                result.shouldBeSuccess()
            }
        }
    }
})
