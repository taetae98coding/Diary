package com.taetae98.diary.domain.usecase.tag.select

import com.taetae98.diary.domain.entity.account.Account
import com.taetae98.diary.domain.repository.SelectTagByMemoRepository
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

class SetHasToPageNoTagMemoUseCaseBddTest : BehaviorSpec(
    {
        val getAccountUseCase = mockk<GetAccountUseCase>()
        val selectTagByMemoRepository = mockk<SelectTagByMemoRepository>(relaxed = true, relaxUnitFun = true)
        val useCase = SetHasToPageNoTagMemoUseCase(
            getAccountUseCase = getAccountUseCase,
            selectTagByMemoRepository = selectTagByMemoRepository,
        )

        Given("Account를 얻을 수 없을 때") {
            every { getAccountUseCase(Unit) } returns flowOf(Result.failure(Exception()))

            When("UseCase를 호출하면") {
                val result = useCase(false)

                Then("실패한다.") {
                    result.shouldBeFailure()
                }
                Then("setHasToPageNoTagMemo 0회 호출한다.") {
                    coVerify(exactly = 0) { selectTagByMemoRepository.setHasToPageNoTagMemo(any(), any()) }
                }
            }
        }

        Given("Account를 얻을 수 있을 때") {
            val account = mockk<Account>(relaxed = true)
            val hasToPage = false

            every { getAccountUseCase(Unit) } returns flowOf(Result.success(account))

            When("UseCase를 호출하면") {
                val result = useCase(hasToPage)

                Then("성공한다.") {
                    result.shouldBeSuccess()
                }
                Then("setHasToPageNoTagMemo 1회 호출한다.") {
                    coVerify(exactly = 1) { selectTagByMemoRepository.setHasToPageNoTagMemo(account.uid, hasToPage) }
                }
            }
        }
    },
)
