package com.taetae98.diary.domain.usecase.memo

import androidx.paging.PagingData
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

class PageFinishMemoUseCaseBddTest : BehaviorSpec({
    val memoRepository = mockk<MemoRepository>()
    val getAccountUseCase = mockk<GetAccountUseCase>()
    val useCase = PageFinishMemoUseCase(
        memoRepository = memoRepository,
        getAccountUseCase = getAccountUseCase,
    )

    Given("Account를 불러오지 못할 때") {
        every { getAccountUseCase(Unit) } returns flowOf(Result.failure(Exception()))

        When("UseCase를 호출하면") {
            val result = useCase(Unit).first()

            Then("에러가 발생한다.") {
                result.shouldBeFailure<NoAccountException>()
            }
        }
    }

    Given("Account를 불러올 수 있을 때") {
        val account = Account.Guest

        every { getAccountUseCase(Unit) } returns flowOf(Result.success(account))
        every { memoRepository.pageFinished(account.uid) } returns flowOf(PagingData.empty())

        When("UseCase를 호출하면") {
            val result = useCase(Unit).first()

            Then("pageFinished가 호출된다.") {
                verify(exactly = 1) { memoRepository.pageFinished(account.uid) }
            }

            Then("성공한다.") {
                result.shouldBeSuccess()
            }
        }
    }
})