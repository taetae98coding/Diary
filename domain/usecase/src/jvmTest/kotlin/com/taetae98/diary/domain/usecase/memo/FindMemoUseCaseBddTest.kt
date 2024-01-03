package com.taetae98.diary.domain.usecase.memo

import com.taetae98.diary.domain.repository.MemoRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.mockk
import kotlinx.coroutines.flow.first

class FindMemoUseCaseBddTest : BehaviorSpec({
    val memoRepository = mockk<MemoRepository>(relaxed = true, relaxUnitFun = true)
    val useCase = FindMemoUseCase(
        memoRepository = memoRepository
    )

    Given("memoId가 null일 때") {
        val memoId: String? = null

        When("UseCase를 호출하면") {
            val result = useCase(memoId)

            Then("null을 리턴한다.") {
                result.first().shouldBeSuccess(null)
            }
        }
    }
})