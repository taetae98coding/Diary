package com.taetae98.diary.domain.usecase.memo.tag

import com.taetae98.diary.domain.entity.memo.MemoTag
import com.taetae98.diary.domain.repository.MemoTagRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk

class DeleteMemoTagUseCaseBddTest : BehaviorSpec({
    val memoTagRepository = mockk<MemoTagRepository>(relaxed = true, relaxUnitFun = true)
    val useCase = DeleteMemoTagUseCase(memoTagRepository = memoTagRepository)

    Given("유효하지 않은 MemoTag가 주어졌을 때") {
        val memoTag = mockk<MemoTag> {
            every { isValidId() } returns false
        }

        When("UseCase를 호출하면") {
            val result = useCase(memoTag)

            Then("성공한다.") {
                result.shouldBeSuccess()
            }

            Then("delete를 호출하지 않는다.") {
                coVerify(exactly = 0) { memoTagRepository.delete(memoTag) }
            }
        }
    }

    Given("유효한 MemoTag가 주어졌을 때") {
        val memoTag = mockk<MemoTag> {
            every { isValidId() } returns true
        }

        When("UseCase를 호출하면") {
            val result = useCase(memoTag)

            Then("성공한다.") {
                result.shouldBeSuccess()
            }

            Then("delete를 호출한다.") {
                coVerify(exactly = 1) { memoTagRepository.delete(memoTag) }
            }
        }
    }
})