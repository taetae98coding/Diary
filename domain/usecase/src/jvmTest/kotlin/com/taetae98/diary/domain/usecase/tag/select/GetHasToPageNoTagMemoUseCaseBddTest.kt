package com.taetae98.diary.domain.usecase.tag.select

import com.taetae98.diary.domain.entity.account.Account
import com.taetae98.diary.domain.exception.NoAccountException
import com.taetae98.diary.domain.repository.SelectTagByMemoRepository
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class GetHasToPageNoTagMemoUseCaseBddTest : BehaviorSpec(
    {
        val getAccountUseCase = mockk<GetAccountUseCase>()
        val selectTagByMemoRepository = mockk<SelectTagByMemoRepository>()
        val useCase = GetHasToPageNoTagMemoUseCase(
            getAccountUseCase = getAccountUseCase,
            selectTagByMemoRepository = selectTagByMemoRepository,
        )

        Given("Account를 얻을 수 없을 때") {
            every { getAccountUseCase(Unit) } returns flowOf(Result.failure(Exception()))

            When("useCase를 호출하면") {
                val result = useCase(Unit).first()

                Then("실패한다.") {
                    result.shouldBeFailure<NoAccountException>()
                }

                Then("hasToPageNoTagMemo 0회 호출한다.") {
                    verify(exactly = 0) { selectTagByMemoRepository.hasToPageNoTagMemo(any()) }
                }
            }
        }

        Given("Account를 얻을 수 있을 때") {
            val account = Account.Guest

            every { getAccountUseCase(Unit) } returns flowOf(Result.success(account))
            every { selectTagByMemoRepository.hasToPageNoTagMemo(account.uid) } returns flowOf(true)

            When("useCase를 호출하면") {
                val result = useCase(Unit).first()

                Then("성공한다.") {
                    result.shouldBeSuccess()
                }

                Then("hasToPageNoTagMemo 1회 호출한다.") {
                    verify(exactly = 1) { selectTagByMemoRepository.hasToPageNoTagMemo(account.uid) }
                }
            }
        }
    },
)