package com.taetae98.diary.domain.usecase.tag

import com.taetae98.diary.domain.entity.tag.TagId
import com.taetae98.diary.domain.repository.TagRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.coVerify
import io.mockk.mockk

class DeleteTagUseCaseBddTest : BehaviorSpec(
    {
        val tagRepository = mockk<TagRepository>(relaxed = true, relaxUnitFun = true)
        val useCase = DeleteTagUseCase(tagRepository = tagRepository)

        Given("유효하지 않은 TagId가 주어질 때") {
            val tagId = TagId("").also { it.isInvalid().shouldBeTrue() }

            When("UseCase를 호출하면") {
                val result = useCase(tagId)

                Then("무시하기 때문에 성공한다.") {
                    result.shouldBeSuccess()
                }

                Then("무시하기 때문에 delete를 호출하지 않는다.") {
                    coVerify(exactly = 0) { tagRepository.delete(tagId.value) }
                }
            }
        }

        Given("유효한 TagId가 주어질 때") {
            val tagId = TagId("valid").also { it.isInvalid().shouldBeFalse() }

            When("UseCase를 호출하면") {
                val result = useCase(tagId)

                Then("성공한다.") {
                    result.shouldBeSuccess()
                }

                Then("delete를 1회 호출한다.") {
                    coVerify(exactly = 1) { tagRepository.delete(tagId.value) }
                }
            }
        }
    },
)