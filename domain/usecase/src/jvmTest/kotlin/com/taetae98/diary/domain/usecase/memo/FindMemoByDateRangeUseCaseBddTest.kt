package com.taetae98.diary.domain.usecase.memo

import com.taetae98.diary.domain.entity.account.Account
import com.taetae98.diary.domain.exception.NoAccountException
import com.taetae98.diary.domain.repository.MemoRepository
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDate

class FindMemoByDateRangeUseCaseBddTest : BehaviorSpec({
    val localDate = LocalDate.fromEpochDays(0)
    val closedRange = localDate..localDate
    val memoRepository = mockk<MemoRepository>()
    val getAccountUseCase = mockk<GetAccountUseCase>()
    val useCase = FindMemoByDateRangeUseCase(
        getAccountUseCase = getAccountUseCase,
        memoRepository = memoRepository,
    )

    Given("Account를 불러오지 못할 때") {
        every { getAccountUseCase(Unit) } returns flowOf(Result.failure(Exception()))

        When("UseCase를 호출하면") {
            val result = useCase(closedRange).first()

            Then("에러가 발생한다.") {
                result.shouldBeFailure<NoAccountException>()
            }
        }
    }

    Given("Account를 불러올 수 있을 때") {
        val account = Account.Guest

        every { getAccountUseCase(Unit) } returns flowOf(Result.success(account))
        every { memoRepository.find(account.uid, closedRange) } returns flowOf(emptyList())

        When("UseCase를 호출하면") {
            val result = useCase(closedRange).first()

            Then("find 호출된다.") {
                verify(exactly = 1) { memoRepository.find(account.uid, closedRange) }
            }

            Then("성공한다.") {
                result.shouldBeSuccess()
            }
        }
    }
})
