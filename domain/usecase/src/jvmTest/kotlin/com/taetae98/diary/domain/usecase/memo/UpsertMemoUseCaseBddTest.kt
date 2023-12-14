package com.taetae98.diary.domain.usecase.memo

import com.taetae98.diary.domain.entity.account.Memo
import com.taetae98.diary.domain.exception.TitleEmptyException
import com.taetae98.diary.domain.repository.MemoRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.every
import io.mockk.mockk

class UpsertMemoUseCaseBddTest : BehaviorSpec({
    val memoRepository = mockk<MemoRepository>(relaxed = true, relaxUnitFun = true)
    val useCase = UpsertMemoUseCase(
        memoRepository = memoRepository
    )

    Given("Title이 Empty인 Memo가 주어졌을 때") {
        val memo = mockk<Memo> {
            every { title } returns ""
        }

        When("UseCase를 호출하면") {
            val result = useCase(memo)

            Then("TitleEmptyException 발생한다.") {
                result.shouldBeFailure<TitleEmptyException>()
            }
        }
    }

    Given("Title이 문자로 이루어진 Memo가 주어졌을 때") {
        val memo = mockk<Memo> {
            every { title } returns "asdf"
        }

        When("UseCase를 호출하면") {
            val result = useCase(memo)

            Then("통과한다.") {
                result.shouldBeSuccess()
            }
        }
    }
})
