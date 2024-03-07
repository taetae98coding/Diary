package com.taetae98.diary.domain.usecase.memo.tag

import com.taetae98.diary.domain.entity.memo.MemoTag
import com.taetae98.diary.domain.repository.MemoTagRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk

class UpsertMemoTagUseCaseBddTest : BehaviorSpec({
    val memoTagRepository = mockk<MemoTagRepository>()
    val useCase = UpsertMemoTagUseCase(memoTagRepository = memoTagRepository)

    Given("유효하지 않은 MemoTag가 주어졌을 때") {
        val memoTag = mockk<MemoTag> {
            every { isInvalid() } returns true
        }

        When("UseCase를 호출하면") {
            val result = useCase(memoTag)

            Then("성공한다.") {
                result.shouldBeSuccess()
            }

            Then("upsert를 호출하지 않는다.") {
                coVerify(exactly = 0) { memoTagRepository.upsert(any<MemoTag>()) }
            }
        }
    }

    Given("유효한 MemoTag가 주어졌을 때") {
        val memoTag = mockk<MemoTag> {
            every { isInvalid() } returns false
        }

        coEvery { memoTagRepository.upsert(memoTag) } returns Unit

        When("UseCase를 호출하면") {
            val result = useCase(memoTag)

            Then("성공한다.") {
                result.shouldBeSuccess()
            }

            Then("upsert를 1회 호출한다.") {
                coVerify(exactly = 1) { memoTagRepository.upsert(memoTag) }
            }
        }
    }
})