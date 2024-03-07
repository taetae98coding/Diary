package com.taetae98.diary.domain.usecase.memo

import com.taetae98.diary.domain.entity.memo.MemoId
import com.taetae98.diary.domain.repository.MemoRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk

class DeleteMemoUseCaseBddTest : BehaviorSpec({
    val memoRepository = mockk<MemoRepository>()
    val useCase = DeleteMemoUseCase(memoRepository = memoRepository)

    Given("유효한 id가 주어질때") {
        val id = MemoId("value").also { it.isInvalid().shouldBeFalse() }

        coEvery { memoRepository.delete(id.value) } returns mockk {
            every { this@mockk.id } returns id.value
        }

        When("UseCase를 호출하면") {
            val result = useCase(id)

            Then("성공한다.") {
                result.shouldBeSuccess()
            }

            Then("id가 동일한 memo를 리턴한다.") {
                result.getOrNull()?.id?.shouldBe(id.value)
            }

            Then("delete가 1회 호출된다.") {
                coVerify(exactly = 1) { memoRepository.delete(id.value) }
            }
        }

        clearMocks(memoRepository)
    }

    Given("유효하지 않은 id가 주어질때") {
        val id = MemoId("").also { it.isInvalid().shouldBeTrue() }

        When("UseCase를 호출하면") {
            val result = useCase(id)

            Then("null을 리턴한다.") {
                result.shouldBeSuccess(null)
            }

            Then("delete가 호출되지 않는다.") {
                coVerify(exactly = 0) { memoRepository.delete(any()) }
            }
        }
    }
})
