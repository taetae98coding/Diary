package com.taetae98.diary.domain.usecase.memo.tag

import com.taetae98.diary.domain.repository.MemoTagRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.mockk
import kotlinx.coroutines.flow.first

class FindMemoTagByMemoIdUseCaseBddTest : BehaviorSpec({
    val repository = mockk<MemoTagRepository>()
    val useCase = FindMemoTagByMemoIdUseCase(
        memoTagRepository = repository
    )

    Given("모든 함수가 실패하는 MemoTagRepository가 주어졌을 때") {
        When("id가 null로 UseCase를 호출하면") {
            val result = useCase(null)

            Then("id가 null이니 emptyList를 리턴한다.") {
                result.first().shouldBeSuccess(emptyList())
            }
        }
    }
})