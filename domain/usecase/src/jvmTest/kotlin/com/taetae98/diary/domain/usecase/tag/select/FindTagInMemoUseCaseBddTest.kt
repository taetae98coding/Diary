package com.taetae98.diary.domain.usecase.tag.select

import com.taetae98.diary.domain.entity.account.Account
import com.taetae98.diary.domain.entity.tag.Tag
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

class FindTagInMemoUseCaseBddTest : BehaviorSpec(
    {
        val getAccountUseCase = mockk<GetAccountUseCase>()
        val selectTagByMemoRepository = mockk<SelectTagByMemoRepository>()
        val useCase = FindTagInMemoUseCase(
            getAccountUseCase = getAccountUseCase,
            selectTagByMemoRepository = selectTagByMemoRepository,
        )

        Given("Account를 얻지 못할 때") {
            every { getAccountUseCase(Unit) } returns flowOf(Result.failure(Exception()))

            When("UseCase를 호출하면") {
                val result = useCase(Unit).first()

                Then("실패한다.") {
                    result.shouldBeFailure<NoAccountException>()
                }

                Then("find 0회 호출한다.") {
                    verify(exactly = 0) { selectTagByMemoRepository.find(any()) }
                }
            }
        }

        Given("Account를 얻을 수 있을 때") {
            val account = Account.Guest
            val list = emptyList<Tag>()

            every { getAccountUseCase(Unit) } returns flowOf(Result.success(account))
            every { selectTagByMemoRepository.find(account.uid) } returns flowOf(list)

            When("UseCase를 호출하면") {
                val result = useCase(Unit).first()

                Then("성공한다.") {
                    result.shouldBeSuccess(list)
                }

                Then("find 1회 호출한다.") {
                    verify(exactly = 1) { selectTagByMemoRepository.find(account.uid) }
                }
            }
        }
    },
)