package com.taetae98.diary.domain.usecase.memo.tag

import com.taetae98.diary.domain.entity.memo.MemoTag
import com.taetae98.diary.domain.repository.MemoTagRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class SwitchMemoTagUseCaseBddTest : BehaviorSpec({
    val memoTagRepository = mockk<MemoTagRepository>(relaxed = true, relaxUnitFun = true)
    val memoTag = mockk<MemoTag>()
    val useCase = SwitchMemoTagUseCase(
        memoTagRepository = memoTagRepository,
    )

    Given("등록되지 않은 MemoTag가 주어졌을 때") {
        coEvery { memoTagRepository.exists(memoTag) } returns false

        When("UseCase를 호출하면") {
            useCase(params = memoTag)

            Then("추가한다.") {
                coVerify(exactly = 1) { memoTagRepository.upsert(memoTag) }
            }

            clearAllMocks(answers = false)
        }
    }

    Given("등록된 MemoTag가 주어졌을 때") {
        coEvery { memoTagRepository.exists(memoTag) } returns true

        When("UseCase를 호출하면") {
            useCase(params = memoTag)

            Then("삭제한다.") {
                coVerify(exactly = 1) { memoTagRepository.delete(memoTag) }
            }

            clearAllMocks(answers = false)
        }
    }
})